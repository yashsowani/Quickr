package com.example.Quickr.ServiceImpl;

import com.example.Quickr.Entities.Document;
import com.example.Quickr.Repository.DocumentRepository;
import com.example.Quickr.Service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document saveToDatabase(Document document) {
        Document result = documentRepository.save(document);
        return result;
    }
}
