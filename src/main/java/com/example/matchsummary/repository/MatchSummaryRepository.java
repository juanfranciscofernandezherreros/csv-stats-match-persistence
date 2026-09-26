package com.example.matchsummary.repository;

import com.example.matchsummary.entity.MatchSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchSummaryRepository extends JpaRepository<MatchSummary, Long> {
}
