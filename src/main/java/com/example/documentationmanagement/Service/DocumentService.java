package com.example.documentationmanagement.Service;

import com.example.documentationmanagement.Repository.DocumentRepository;
import com.example.documentationmanagement.Repository.FolderRepository;
import com.example.documentationmanagement.entities.Document;
import com.example.documentationmanagement.entities.DocumentStatus;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {
    DocumentRepository documentRepository;
    private final Directory index;
    private final IndexWriter writer;

    // Constructor
    public DocumentService(DocumentRepository documentRepository) throws IOException {
        this.documentRepository = documentRepository;
        Path indexPath = Paths.get("lucene-index");
        System.out.println("Lucene index path: " + indexPath.toAbsolutePath()); // Print path
        this.index = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        this.writer = new IndexWriter(index, config);
    }

    // ... (rest of your existing methods) ...

    @PostConstruct
    public void init() throws IOException {
        indexDocuments(); // Initial indexing on startup
    }

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







    // ... (rest of your existing methods) ...

    @Scheduled(cron = "0 0 * * * ?") // Run every day at midnight
    public void scheduledIndexDocuments() throws IOException {
        indexDocuments();
    }

    public synchronized void indexDocuments() throws IOException {
        try {
            writer.deleteAll();
            System.out.println("Simplified indexing test!");
            org.apache.lucene.document.Document luceneDoc = new org.apache.lucene.document.Document();
            luceneDoc.add(new TextField("content", "test document", Field.Store.YES));
            writer.addDocument(luceneDoc);
            writer.commit();
            System.out.println("Simplified indexing complete!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Document> searchDocuments(String queryStr) throws IOException, ParseException {
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());
        Query query = parser.parse(queryStr);
        ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;

        List<Document> results = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            org.apache.lucene.document.Document hitDoc = searcher.doc(hit.doc);
            Document resultDoc = new Document();
            resultDoc.setTitle(hitDoc.get("title"));
            resultDoc.setContent(hitDoc.get("content"));
            results.add(resultDoc);
        }
        reader.close();
        return results;
    }

    public void closeWriter() throws IOException {
        writer.close();
    }


    public List<Document> getGeneralDocuments() {
        return documentRepository.findByFolderIdAndIsDeletedFalse(0L); // Fetch documents with folderId = 0
    }



    public Document createDocumentWithContent(Document document) {
        document.setVersionNumber(1); // Initial version
        document.setStatus(DocumentStatus.DRAFT);
        document.setStoragePath(null); // No file path
        return documentRepository.save(document);
    }

    @Transactional
    public Document updateDocumentContent(Long documentId, String newContent) {
        Document document = getDocumentById(documentId);
        if (!document.isEditable()) {
            throw new UnsupportedOperationException("This document is not editable.");
        }
        // Store previous version
        document.setPreviousVersions(
                (document.getPreviousVersions() == null ? "" : document.getPreviousVersions() + ",") + document.getContent()
        );
        document.setContent(newContent);
        document.setVersionNumber(document.getVersionNumber() + 1);
        return documentRepository.save(document);
    }


    public Document createDocument(Document document) {
        document.setVersionNumber(1); // Initial version
        document.setStatus(DocumentStatus.DRAFT);
        document.setStoragePath(null); // No file path
        return documentRepository.save(document);
    }
}
