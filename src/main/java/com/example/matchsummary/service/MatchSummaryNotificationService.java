package com.example.matchsummary.service;
import com.example.csvwatcher.watcher.*;
import com.example.matchsummary.entity.*;
import com.example.matchsummary.repository.CsvStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class MatchSummaryNotificationService {
 private final CsvStatusRepository statuses;
 private final KafkaTemplate<FileSuccessKey,FileSuccessValue> successes;
 private final KafkaTemplate<FileErrorKey,FileErrorValue> errors;
 private final String successTopic,errorTopic;
 public MatchSummaryNotificationService(CsvStatusRepository statuses,
   KafkaTemplate<FileSuccessKey,FileSuccessValue> successes,KafkaTemplate<FileErrorKey,FileErrorValue> errors,
   @Value("${app.kafka.topics.file-success}") String successTopic,@Value("${app.kafka.topics.file-errors}") String errorTopic){
  this.statuses=statuses;this.successes=successes;this.errors=errors;this.successTopic=successTopic;this.errorTopic=errorTopic;
 }
 public void success(String matchId,String fileName,String filePath,String fileType,MatchSummary summary){
  Instant now=Instant.now(); save(matchId,fileName,filePath,fileType,CsvStatus.Status.SUCCESS,now);
  successes.send(successTopic,FileSuccessKey.newBuilder().setMatchId(matchId).build(),
   FileSuccessValue.newBuilder().setMatchId(matchId).setFileName(fileName).setFilePath(filePath).setFileType(fileType)
    .setSuccessMessage(summary.toString()).setTimestamp(now).build()).join();
 }
 public void error(String matchId,String fileName,String filePath,String fileType,String message){
  Instant now=Instant.now(); save(matchId,fileName,filePath,fileType,CsvStatus.Status.ERROR,now);
  errors.send(errorTopic,FileErrorKey.newBuilder().setMatchId(matchId).build(),
   FileErrorValue.newBuilder().setMatchId(matchId).setFileName(fileName).setFilePath(filePath).setFileType(fileType)
    .setErrorMessage(message==null?"Unknown error":message).setTimestamp(now).build()).join();
 }
 private void save(String matchId,String fileName,String filePath,String fileType,CsvStatus.Status s,Instant now){
  statuses.save(CsvStatus.builder().matchId(matchId).fileName(fileName).filePath(filePath).fileType(fileType).status(s).timestamp(now).build());
 }
}
