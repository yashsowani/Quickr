package com.example.Quickr.ServiceImpl;

import com.example.Quickr.Entities.ErrorLogger;
import com.example.Quickr.Repository.ErrorLoggerRepository;
import com.example.Quickr.Service.AuditErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditErrorServiceImpl implements AuditErrorService {

    @Autowired
    public ErrorLoggerRepository errorLoggerRepository;

    @Override
    public ErrorLogger saveError(ErrorLogger errorLogger) {
        ErrorLogger result = errorLoggerRepository.save(errorLogger);
        return result;
    }
}
