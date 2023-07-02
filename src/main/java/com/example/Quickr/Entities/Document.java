package com.example.Quickr.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 512, nullable = false)
    private String name;
    @Column
    private Long size;
    @Column(name = "uploadDt")
    private Date uploadDt;

    @Lob
    private byte[] content;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "session_id")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getUploadDt() {
        return uploadDt;
    }

    public void setUploadDt(Date uploadDt) {
        this.uploadDt = uploadDt;
    }

    public byte[] getContent() {
        return content;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Document() {
    }

    public Document(Long id, String name, Long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }
}
