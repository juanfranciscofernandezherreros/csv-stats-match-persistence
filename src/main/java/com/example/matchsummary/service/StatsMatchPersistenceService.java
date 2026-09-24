package com.example.matchsummary.service;

import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.mapper.MatchSummaryMapper;
import com.example.matchsummary.repository.MatchSummaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatsMatchPersistenceService {
    private final MatchSummaryRepository summaries;
    private final MatchSummaryMapper mapper;

    public StatsMatchPersistenceService(MatchSummaryRepository summaries, MatchSummaryMapper mapper) {
        this.summaries = summaries;
        this.mapper = mapper;
    }

    @Transactional
    public void handle(StatsMatchValue value) {
        if (summaries.existsByMatchId(value.getMatchId())) {
            return;
        }
        summaries.saveAndFlush(mapper.toEntity(value));
    }
}
