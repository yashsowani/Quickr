package com.example.Quickr.Repository;

import com.example.Quickr.Entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Query("Select new Document(d.id, d.name, d.size) from Document d where d.isActive =true ORDER BY d.uploadDt DESC")
    List<Document> findAll();

    @Query("Select new Document(d.id, d.name, d.size) from Document d where d.isActive =true and d.sessionId=?1 ORDER BY d.uploadDt DESC")
    List<Document> findAllBySession(String sessionId);
}
