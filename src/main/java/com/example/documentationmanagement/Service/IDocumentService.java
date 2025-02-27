package com.example.documentationmanagement.Service;

import com.example.documentationmanagement.entities.Document;

import java.util.List;

public interface IDocumentService {
    public Document uploadDocument(Document document);
    public Document getDocumentById(Long documentId);
    public List<Document> getDocumentsByProject(Long projectId);
    public List<Document> getDocumentsByFolder(Long folderId);
    public Document updateDocument(Long documentId, String newContent, String newTags, String newDescription);
    public void deleteDocument(Long documentId);
    public Document revertToPreviousVersion(Long documentId);
    public void restoreDocument(Long documentId);
    public boolean checkPermissions(Long documentId, Long userId, String action);
    public List<Document> getRecentDocuments();
    public Document updateDocument(Document document);

}
