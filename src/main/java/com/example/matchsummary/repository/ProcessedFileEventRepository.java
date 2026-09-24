package com.example.matchsummary.repository;
import com.example.matchsummary.entity.ProcessedFileEvent;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProcessedFileEventRepository extends JpaRepository<ProcessedFileEvent,String>{}
