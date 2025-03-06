package com.example.documentationmanagement.Controller;

import com.example.documentationmanagement.Service.DocumentService;
import com.example.documentationmanagement.Service.IDocumentService;
import com.example.documentationmanagement.entities.Document;
import com.example.documentationmanagement.entities.DocumentStatus;
import com.example.documentationmanagement.entities.DocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Tag(name = "Documents")
@RestController
@AllArgsConstructor
@RequestMapping("documents")
public class DocumentController {
    DocumentService documentService;
    private final String uploadDir = "/uploads";

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@RequestParam(value = "file", required = false) MultipartFile file,
                                                   @RequestParam("title") String title,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("status") String status,
                                                   @RequestParam(value = "folderId", defaultValue = "0") Long folderId, // Default to 0
                                                   @RequestParam("tags") String tags,
                                                   @RequestParam("description") String description) throws IOException {
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        Document document = new Document();
        document.setTitle(title);
        document.setStatus(DocumentStatus.valueOf(status));
        document.setVersionNumber(1);
        document.setType(DocumentType.valueOf(type));
        document.setFolderId(folderId); // Use the provided folderId (defaults to 0)
        document.setTags(tags);
        document.setDescription(description);

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath);
            document.setStoragePath(filePath.toString());
        } else {
            document.setStoragePath(null); // No file provided
        }

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


    @PostMapping("/create-document")
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        Document savedDocument = documentService.createDocument(document);
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
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

    @GetMapping("/recent-files")
    public ResponseEntity<List<Document>> getRecentFiles() {
        List<Document> recentDocuments = documentService.getRecentDocuments();
        return new ResponseEntity<>(recentDocuments, HttpStatus.OK);
    }


    @GetMapping("/view-file/{documentId}")
    public ResponseEntity<FileSystemResource> viewFile(@PathVariable Long documentId) {
        // Retrieve the document from the database using documentId
        // Assuming you have a documentService to fetch documents by ID
        Document document = documentService.getDocumentById(documentId);

        if (document == null || document.getStoragePath() == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(document.getStoragePath());
        File file = filePath.toFile();

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(file);
        MediaType mediaType;

        try {
            mediaType = MediaType.parseMediaType(Files.probeContentType(filePath));
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM; // Default if type cannot be determined
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    @GetMapping("/get-documents-by-folder/{folderId}") // <--- Added @GetMapping
    public ResponseEntity<List<Document>> getDocumentsByFolder(@PathVariable Long folderId) {
        System.out.println("DocumentController: getDocumentsByFolder called with folderId: " + folderId);
        List<Document> documents = documentService.getDocumentsByFolder(folderId);
        System.out.println("DocumentController: Documents retrieved. Size: " + documents.size());
        if (documents != null && !documents.isEmpty()) {
            System.out.println("DocumentController: Returning documents.");
            return ResponseEntity.ok(documents);
        } else {
            System.out.println("DocumentController: No documents found.");
            return ResponseEntity.noContent().build();
        }
    }





    @PutMapping("/update-document/{documentId}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long documentId,
                                                   @RequestParam(value = "file", required = false) MultipartFile file,
                                                   @RequestParam("title") String title,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("status") String status,
                                                   @RequestParam("tags") String tags,
                                                   @RequestParam("description") String description) throws IOException {
        System.out.println("Updating document ID: " + documentId);
        System.out.println("Title: " + title);
        System.out.println("Type: " + type);
        System.out.println("Status: " + status);
        System.out.println("Tags: " + tags);
        System.out.println("Description: " + description);
        try{
            Document existingDocument = documentService.getDocumentById(documentId);
            if (existingDocument == null) {
                System.out.println("Document not found.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            System.out.println("Document before update: " + existingDocument.toString());
            existingDocument.setTitle(title);
            existingDocument.setType(DocumentType.valueOf(type));
            existingDocument.setStatus(DocumentStatus.valueOf(status));
            existingDocument.setTags(tags);
            existingDocument.setDescription(description);
            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath);
                existingDocument.setStoragePath(filePath.toString());
            }

            // Increment version number
            existingDocument.setVersionNumber(existingDocument.getVersionNumber() + 1);

            System.out.println("Document after update: " + existingDocument.toString());
            Document updatedDocument = documentService.updateDocument(existingDocument);
            System.out.println("Document after save: " + updatedDocument.toString());
            return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
        } catch (Exception e){
            System.out.println("An error has occured : " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/search")
    public List<Document> search(@RequestParam String query) throws Exception {
        documentService.indexDocuments(); // Reindex on each search for simplicity (can be optimized)
        return documentService.searchDocuments(query);
    }


    @GetMapping("/general-documents")
    public ResponseEntity<List<Document>> getGeneralDocuments() {
        List<Document> documents = documentService.getGeneralDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }



    @PostMapping("/create-document-with-content")
    public ResponseEntity<Document> createDocumentWithContent(@RequestBody Document document) {
        Document savedDocument = documentService.createDocumentWithContent(document);
        return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
    }

    @PutMapping("/update-document-content/{documentId}")
    public ResponseEntity<Document> updateDocumentContent(@PathVariable Long documentId, @RequestBody Map<String, String> request) {
        String newContent = request.get("content");
        Document updatedDocument = documentService.updateDocumentContent(documentId, newContent);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }
}