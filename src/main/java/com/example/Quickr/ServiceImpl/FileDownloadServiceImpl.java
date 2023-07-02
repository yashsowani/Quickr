package com.example.Quickr.ServiceImpl;

import com.example.Quickr.Entities.Document;
import com.example.Quickr.Repository.DocumentRepository;
import com.example.Quickr.Service.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileDownloadServiceImpl implements FileDownloadService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Optional<Document> downloadById(Long id) {
        Optional<Document> result = documentRepository.findById(id);
        return result;
    }

    @Override
    public List<Document> downloadBySession(String sessionId) {
        List<Document> result = documentRepository.findAllBySession(sessionId);
        return result;
    }
}
