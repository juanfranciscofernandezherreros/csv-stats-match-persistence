package com.example.matchsummary.mapper;
import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.entity.MatchSummary;
import org.springframework.stereotype.Component;
@Component
public class MatchSummaryMapper {
 public MatchSummary toEntity(StatsMatchValue v){
  return MatchSummary.builder().matchId(v.getMatchId()).date(v.getDate()).homeName(v.getHomeName()).homeImage(v.getHomeImage())
   .awayName(v.getAwayName()).awayImage(v.getAwayImage()).resultHome(v.getResultHome()).resultAway(v.getResultAway())
   .totalLocal(v.getTotalLocal()).firstLocal(v.getFirstLocal()).secondLocal(v.getSecondLocal()).thirdLocal(v.getThirdLocal())
   .fourthLocal(v.getFourthLocal()).extraLocal(v.getExtraLocal()).totalAway(v.getTotalAway()).firstAway(v.getFirstAway())
   .secondAway(v.getSecondAway()).thirdAway(v.getThirdAway()).fourthAway(v.getFourthAway()).extraAway(v.getExtraAway()).build();
 }
}
