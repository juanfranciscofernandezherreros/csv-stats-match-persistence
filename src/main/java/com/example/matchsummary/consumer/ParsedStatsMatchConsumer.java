package com.example.matchsummary.consumer;
import com.example.matchsummary.avro.*;
import com.example.matchsummary.service.StatsMatchPersistenceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
public class ParsedStatsMatchConsumer {
 private final StatsMatchPersistenceService service;
 public ParsedStatsMatchConsumer(StatsMatchPersistenceService service){this.service=service;}
 @KafkaListener(topics="${app.kafka.topics.parsed-stats-match}",groupId="${spring.kafka.consumer.group-id}")
 public void listen(ConsumerRecord<StatsMatchKey,StatsMatchValue> record){service.handle(record.value());}
}
