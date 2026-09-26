package com.example.matchsummary.repository;

import com.example.matchsummary.entity.MatchSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MatchSummaryUpsertRepository {

    private static final String UPSERT_SQL = """
            INSERT INTO match_summary (
                match_id, date, home_name, home_image, away_name, away_image,
                result_home, result_away,
                total_local, first_local, second_local, third_local, fourth_local, extra_local,
                total_away, first_away, second_away, third_away, fourth_away, extra_away
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (match_id) DO UPDATE SET
                date = EXCLUDED.date,
                home_name = EXCLUDED.home_name,
                home_image = EXCLUDED.home_image,
                away_name = EXCLUDED.away_name,
                away_image = EXCLUDED.away_image,
                result_home = EXCLUDED.result_home,
                result_away = EXCLUDED.result_away,
                total_local = EXCLUDED.total_local,
                first_local = EXCLUDED.first_local,
                second_local = EXCLUDED.second_local,
                third_local = EXCLUDED.third_local,
                fourth_local = EXCLUDED.fourth_local,
                extra_local = EXCLUDED.extra_local,
                total_away = EXCLUDED.total_away,
                first_away = EXCLUDED.first_away,
                second_away = EXCLUDED.second_away,
                third_away = EXCLUDED.third_away,
                fourth_away = EXCLUDED.fourth_away,
                extra_away = EXCLUDED.extra_away
            """;

    private final JdbcTemplate jdbcTemplate;

    public MatchSummaryUpsertRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void upsert(MatchSummary summary) {
        jdbcTemplate.update(
                UPSERT_SQL,
                summary.getMatchId(),
                summary.getDate(),
                summary.getHomeName(),
                summary.getHomeImage(),
                summary.getAwayName(),
                summary.getAwayImage(),
                summary.getResultHome(),
                summary.getResultAway(),
                summary.getTotalLocal(),
                summary.getFirstLocal(),
                summary.getSecondLocal(),
                summary.getThirdLocal(),
                summary.getFourthLocal(),
                summary.getExtraLocal(),
                summary.getTotalAway(),
                summary.getFirstAway(),
                summary.getSecondAway(),
                summary.getThirdAway(),
                summary.getFourthAway(),
                summary.getExtraAway()
        );
    }
}
