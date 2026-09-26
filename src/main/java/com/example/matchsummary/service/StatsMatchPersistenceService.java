package com.example.matchsummary.service;

import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.mapper.MatchSummaryMapper;
import com.example.matchsummary.repository.MatchSummaryUpsertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatsMatchPersistenceService {
    private final MatchSummaryUpsertRepository summaries;
    private final MatchSummaryMapper mapper;

    public StatsMatchPersistenceService(MatchSummaryUpsertRepository summaries, MatchSummaryMapper mapper) {
        this.summaries = summaries;
        this.mapper = mapper;
    }

    @Transactional
    public void handle(StatsMatchValue value) {
        summaries.upsert(mapper.toEntity(value));
    }
}
