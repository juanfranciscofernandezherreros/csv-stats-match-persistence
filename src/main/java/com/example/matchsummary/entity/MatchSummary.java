package com.example.matchsummary.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="match_summary") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchSummary {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="match_id",nullable=false,unique=true) private String matchId;
 private String date; private String homeName; private String homeImage; private String awayName; private String awayImage;
 private String resultHome; private String resultAway; private String totalLocal; private String firstLocal; private String secondLocal;
 private String thirdLocal; private String fourthLocal; private String extraLocal; private String totalAway; private String firstAway;
 private String secondAway; private String thirdAway; private String fourthAway; private String extraAway;
}
