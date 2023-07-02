package com.example.Quickr.Service;

import com.example.Quickr.Entities.Document;

public interface FileUploadService {
    public Document saveToDatabase(Document document);
}
