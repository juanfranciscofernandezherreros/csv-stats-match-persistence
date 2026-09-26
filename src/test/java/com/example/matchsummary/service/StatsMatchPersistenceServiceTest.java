package com.example.matchsummary.service;

import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.entity.MatchSummary;
import com.example.matchsummary.mapper.MatchSummaryMapper;
import com.example.matchsummary.repository.MatchSummaryUpsertRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StatsMatchPersistenceServiceTest {

    @Test
    void upsertsMappedSummaryAtomically() {
        var summaries = mock(MatchSummaryUpsertRepository.class);
        var mapper = mock(MatchSummaryMapper.class);
        var value = mock(StatsMatchValue.class);
        var entity = new MatchSummary();

        when(mapper.toEntity(value)).thenReturn(entity);

        new StatsMatchPersistenceService(summaries, mapper).handle(value);

        verify(summaries).upsert(entity);
    }
}
