package com.example.documentationmanagement.Controller;

import com.example.documentationmanagement.Service.IDocumentService;
import com.example.documentationmanagement.entities.Document;
import com.example.documentationmanagement.entities.DocumentStatus;
import com.example.documentationmanagement.entities.DocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Tag(name = "Documents")
@RestController
@AllArgsConstructor
@RequestMapping("documents")
public class DocumentController {
 IDocumentService documentService;

    // Hardcoded file upload directory
    private final String uploadDir = "/uploads";

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("title") String title,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("status") String status) throws IOException {

        // Ensure the upload directory exists
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();  // Create directories if they don't exist
        }

        // Generate a unique filename (e.g., timestamp + original filename)
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // Save the file locally
        Files.copy(file.getInputStream(), filePath);

        // Create a Document entity and set the file metadata
        Document document = new Document();
        document.setTitle(title);
        document.setStoragePath(filePath.toString());  // Save the file path in DB
        document.setStatus(DocumentStatus.valueOf(status));  // Set status from request
        document.setVersionNumber(1);
        document.setType(DocumentType.valueOf(type));  // Set the type from request

        // Save the document metadata to the database
        Document savedDocument = documentService.uploadDocument(document);

        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }



    @GetMapping("/get-document/{documentId}")
    public ResponseEntity<Document> getDocument(@PathVariable Long documentId) {
        Document document = documentService.getDocumentById(documentId);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @GetMapping("/get-project-documents/{projectId}")
    public ResponseEntity<List<Document>> getDocumentsByProject(@PathVariable Long projectId) {
        List<Document> documents = documentService.getDocumentsByProject(projectId);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @GetMapping("/get-document-by-folder/{folderId}")
    public ResponseEntity<List<Document>> getDocumentsByFolder(@PathVariable Long folderId) {
        List<Document> documents = documentService.getDocumentsByFolder(folderId);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PutMapping("/update-document/{documentId}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long documentId,
                                                   @RequestParam("newContent") String newContent) {
        Document updatedDocument = documentService.updateDocument(documentId, newContent);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    @DeleteMapping("/delete-document/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{documentId}/restore")
    public ResponseEntity<Void> restoreDocument(@PathVariable Long documentId) {
        documentService.restoreDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{documentId}/revert")
    public ResponseEntity<Document> revertDocument(@PathVariable Long documentId) {
        Document revertedDocument = documentService.revertToPreviousVersion(documentId);
        return new ResponseEntity<>(revertedDocument, HttpStatus.OK);
    }
}
