package com.example.matchsummary.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
@Entity @Table(name="csv_status") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CsvStatus {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String matchId; private String fileName; private String filePath; private String fileType;
 @Enumerated(EnumType.STRING) private Status status; private Instant timestamp;
 public enum Status { SUCCESS, ERROR, DUPLICATED }
}
