package com.example.matchsummary.consumer;

import com.example.matchsummary.avro.StatsMatchKey;
import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.service.StatsMatchPersistenceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ParsedStatsMatchConsumerTest {

    @Test
    void persistsSingleSummaryPerKafkaRecord() {
        StatsMatchPersistenceService service = mock(StatsMatchPersistenceService.class);
        ParsedStatsMatchConsumer consumer = new ParsedStatsMatchConsumer(service);
        StatsMatchValue value = mock(StatsMatchValue.class);

        ConsumerRecord<StatsMatchKey, StatsMatchValue> record =
                new ConsumerRecord<>("stats-match.parsed", 0, 0L, null, value);

        consumer.listen(record);

        verify(service).handle(value);
    }
}
