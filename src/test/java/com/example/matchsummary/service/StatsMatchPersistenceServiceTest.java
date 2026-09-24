package com.example.matchsummary.service;

import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.entity.MatchSummary;
import com.example.matchsummary.mapper.MatchSummaryMapper;
import com.example.matchsummary.repository.MatchSummaryRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class StatsMatchPersistenceServiceTest {
    @Test
    void existingMatchIsIgnored() {
        var summaries = mock(MatchSummaryRepository.class);
        var mapper = mock(MatchSummaryMapper.class);
        var value = mock(StatsMatchValue.class);
        when(value.getMatchId()).thenReturn("m1");
        when(summaries.existsByMatchId("m1")).thenReturn(true);

        new StatsMatchPersistenceService(summaries, mapper).handle(value);

        verify(summaries).existsByMatchId("m1");
        verifyNoInteractions(mapper);
        verify(summaries, never()).saveAndFlush(any());
    }

    @Test
    void newMatchIsPersisted() {
        var summaries = mock(MatchSummaryRepository.class);
        var mapper = mock(MatchSummaryMapper.class);
        var value = mock(StatsMatchValue.class);
        var entity = new MatchSummary();
        when(value.getMatchId()).thenReturn("m1");
        when(mapper.toEntity(value)).thenReturn(entity);

        new StatsMatchPersistenceService(summaries, mapper).handle(value);

        verify(summaries).saveAndFlush(entity);
    }
}
