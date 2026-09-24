package com.example.matchsummary.service;
import com.example.matchsummary.avro.StatsMatchValue;
import com.example.matchsummary.entity.ProcessedFileEvent;
import com.example.matchsummary.mapper.MatchSummaryMapper;
import com.example.matchsummary.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
public class StatsMatchPersistenceService {
 private final MatchSummaryRepository summaries; private final ProcessedFileEventRepository events;
 private final MatchSummaryMapper mapper; private final MatchSummaryNotificationService notifications;
 public StatsMatchPersistenceService(MatchSummaryRepository summaries,ProcessedFileEventRepository events,
   MatchSummaryMapper mapper,MatchSummaryNotificationService notifications){
  this.summaries=summaries;this.events=events;this.mapper=mapper;this.notifications=notifications;
 }
 @Transactional
 public void handle(StatsMatchValue value){
  String eventId=value.getSourceEventId();
  if(events.existsById(eventId)) return;
  if("FAILED".equals(value.getEventType())){
   notifications.error(value.getMatchId(),value.getFileName(),value.getFilePath(),value.getFileType(),value.getError());
   events.save(new ProcessedFileEvent(eventId,Instant.now()));
   return;
  }
  if(!"PARSED".equals(value.getEventType())) throw new IllegalArgumentException("Unsupported eventType: "+value.getEventType());
  if(summaries.existsByMatchId(value.getMatchId())){
   events.save(new ProcessedFileEvent(eventId,Instant.now()));
   return;
  }
  var summary=summaries.saveAndFlush(mapper.toEntity(value));
  notifications.success(value.getMatchId(),value.getFileName(),value.getFilePath(),value.getFileType(),summary);
  events.save(new ProcessedFileEvent(eventId,Instant.now()));
 }
}
