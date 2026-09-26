package com.example.matchsummary;

import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.repository.MatchSummaryRepository;
import com.example.matchsummary.service.StatsMatchPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(properties = {
        "spring.kafka.listener.auto-startup=false",
        "spring.kafka.bootstrap-servers=localhost:9092",
        "spring.kafka.properties.schema.registry.url=mock://match-persistence-it"
})
class StatsMatchPersistenceIT {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("match_summary")
                    .withUsername("match")
                    .withPassword("match");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    StatsMatchPersistenceService service;

    @Autowired
    MatchSummaryRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void firstImportCreatesSummary() {
        service.handle(value("source-1", "Home", "90"));

        assertEquals(1, repository.count());
        var saved = repository.findAll().get(0);
        assertEquals("m1", saved.getMatchId());
        assertEquals("Home", saved.getHomeName());
        assertEquals("90", saved.getResultHome());
    }

    @Test
    void reimportUpdatesCurrentSummaryInsteadOfIgnoringCorrection() {
        service.handle(value("source-1", "Home", "90"));
        service.handle(value("source-2", "Home corrected", "91"));

        assertEquals(1, repository.count());
        var saved = repository.findAll().get(0);
        assertEquals("Home corrected", saved.getHomeName());
        assertEquals("91", saved.getResultHome());
    }

    @Test
    void concurrentRedeliveryKeepsSingleSummary() throws Exception {
        StatsMatchValue event = value("source-concurrent", "Home", "90");
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<?> first = executor.submit(() -> handleAfter(start, event));
            Future<?> second = executor.submit(() -> handleAfter(start, event));

            start.countDown();
            first.get();
            second.get();

            assertEquals(1, repository.count());
            var saved = repository.findAll().get(0);
            assertEquals("Home", saved.getHomeName());
            assertEquals("90", saved.getResultHome());
        } finally {
            executor.shutdownNow();
        }
    }

    private void handleAfter(CountDownLatch start, StatsMatchValue event) {
        try {
            start.await();
            service.handle(event);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(interrupted);
        }
    }

    private StatsMatchValue value(String sourceEventId, String homeName, String resultHome) {
        return StatsMatchValue.newBuilder()
                .setSourceEventId(sourceEventId)
                .setMatchId("m1")
                .setHomeName(homeName)
                .setAwayName("Away")
                .setResultHome(resultHome)
                .setResultAway("80")
                .build();
    }
}
