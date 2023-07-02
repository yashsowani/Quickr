package com.example.Quickr.Service;

import com.example.Quickr.Entities.Document;

import java.util.List;
import java.util.Optional;

public interface FileDownloadService {
    public Optional<Document> downloadById(Long id);

    public List<Document> downloadBySession(String sessionId);
}
