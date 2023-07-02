package com.example.Quickr.Service;

import com.example.Quickr.Entities.Document;

import java.util.Optional;

public interface FileDeleteService {

    public Optional<Document> deleteDatabase(Long id);
}
