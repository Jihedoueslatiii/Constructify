package com.example.documentationmanagement.Repository;

import com.example.documentationmanagement.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findByProjectIdAndIsDeletedFalse(Long projectId);
    List<Document> findByFolderIdAndIsDeletedFalse(Long folderId);
    List<Document> findTop10ByOrderByCreatedAtDesc();
    List<Document> findByFolderId(int folderId);
}
