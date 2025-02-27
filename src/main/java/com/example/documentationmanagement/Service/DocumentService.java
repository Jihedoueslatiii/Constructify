package com.example.documentationmanagement.Service;

import com.example.documentationmanagement.Repository.DocumentRepository;
import com.example.documentationmanagement.Repository.FolderRepository;
import com.example.documentationmanagement.entities.Document;
import com.example.documentationmanagement.entities.DocumentStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DocumentService implements IDocumentService {
    DocumentRepository documentRepository;


    /**
     * Upload a new document (either an editable text document or a file-based document).
     */

    public Document uploadDocument(Document document) {
        document.setVersionNumber(1); // Initial version
        document.setStatus(DocumentStatus.DRAFT);
        return documentRepository.save(document);
    }


    /**
     * Retrieve a document by its ID.
     */
    public Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with ID: " + documentId));
    }

    /**
     * Retrieve all documents for a specific project.
     */
    public List<Document> getDocumentsByProject(Long projectId) {
        return documentRepository.findByProjectIdAndIsDeletedFalse(projectId);
    }

    /**
     * Retrieve all documents in a specific folder.
     */
    public List<Document> getDocumentsByFolder(Long folderId) {
        System.out.println("DocumentService: getDocumentsByFolder called with folderId: " + folderId);
        List<Document> documents = documentRepository.findByFolderIdAndIsDeletedFalse(folderId);
        System.out.println("DocumentService: Documents retrieved. Size: " + documents.size());
        return documents;
    }
    /**
     * Update an existing document (editable content).
     */
    @Transactional
    public Document updateDocument(Long documentId, String newContent, String newTags, String newDescription) {
        Document document = getDocumentById(documentId);
        if (!document.isEditable()) {
            throw new UnsupportedOperationException("This document is not editable.");
        }
        // Store previous version
        document.setPreviousVersions(
                (document.getPreviousVersions() == null ? "" : document.getPreviousVersions() + ",") + document.getContent()
        );
        document.setContent(newContent);
        document.setTags(newTags);
        document.setDescription(newDescription);  // Update description
        document.setVersionNumber(document.getVersionNumber() + 1);
        return documentRepository.save(document);
    }

    /**
     * Soft delete a document (mark as deleted instead of permanent removal).
     */
    @Transactional
    public void deleteDocument(Long documentId) {
        Document document = getDocumentById(documentId);
        document.setDeleted(true); // Mark as deleted
        documentRepository.save(document);
    }

    /**
     * Restore a soft-deleted document.
     */
    @Transactional
    public void restoreDocument(Long documentId) {
        Document document = getDocumentById(documentId);
        document.setDeleted(false); // Restore
        documentRepository.save(document);
    }

    /**
     * Revert to a previous version of a document.
     */
    @Transactional
    public Document revertToPreviousVersion(Long documentId) {
        Document document = getDocumentById(documentId);
        if (document.getPreviousVersions() == null || document.getPreviousVersions().isEmpty()) {
            throw new IllegalStateException("No previous versions available for this document.");
        }

        // Extract last saved version
        String[] versions = document.getPreviousVersions().split(",");
        String lastVersion = versions[versions.length - 1];

        // Remove reverted version from history
        document.setPreviousVersions(String.join(",", List.of(versions).subList(0, versions.length - 1)));
        document.setContent(lastVersion);
        document.setVersionNumber(document.getVersionNumber() - 1);
        return documentRepository.save(document);
    }

    /**
     * Check and enforce document permissions (optional based on user roles).
     */
    public boolean checkPermissions(Long documentId, Long userId, String action) {
        Document document = getDocumentById(documentId);

        // Example: Simulate permissions by document status or action type (e.g., can edit only documents in 'draft' status)
        if ("edit".equals(action)) {
            return document.getStatus() == DocumentStatus.DRAFT;
        }

        if ("delete".equals(action)) {
            return document.getStatus() == DocumentStatus.DRAFT; // Simulate permission to delete only draft documents
        }

        return true; // Default to allowing any other action
    }


    public List<Document> getRecentDocuments() {
        // Call the repository method to get the most recent documents
        return documentRepository.findTop10ByOrderByCreatedAtDesc();
    }
    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

}
