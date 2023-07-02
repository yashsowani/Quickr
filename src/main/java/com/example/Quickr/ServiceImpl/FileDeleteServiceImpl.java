package com.example.Quickr.ServiceImpl;

import com.example.Quickr.Entities.Document;
import com.example.Quickr.Repository.DocumentRepository;
import com.example.Quickr.Service.FileDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FileDeleteServiceImpl implements FileDeleteService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Optional<Document> deleteDatabase(Long id) {
        Optional<Document> result = documentRepository.findById(id);
        Document currentFile = result.get();
        currentFile.setActive(false);
        return result;
    }
}
