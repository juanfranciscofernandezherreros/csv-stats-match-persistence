package com.example.matchsummary.service;
import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.mapper.MatchSummaryMapper;
import com.example.matchsummary.repository.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
class StatsMatchPersistenceServiceTest {
 @Test void alreadyProcessedEventIsIgnored(){
  var summaries=mock(MatchSummaryRepository.class);var events=mock(ProcessedFileEventRepository.class);
  var mapper=mock(MatchSummaryMapper.class);var notifications=mock(MatchSummaryNotificationService.class);
  when(events.existsById("e1")).thenReturn(true);
  var service=new StatsMatchPersistenceService(summaries,events,mapper,notifications);
  var v=mock(StatsMatchValue.class);when(v.getSourceEventId()).thenReturn("e1");
  service.handle(v);verifyNoInteractions(summaries,mapper,notifications);
 }
}
