package com.example.Quickr.Service;

import com.example.Quickr.Entities.ErrorLogger;

public interface AuditErrorService {

    public ErrorLogger saveError(ErrorLogger errorLogger);
}
