package com.example.documentationmanagement.Repository;

import com.example.documentationmanagement.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder,Long> {
    // Custom query method to find folders that are not deleted
    List<Folder> findByIsDeletedFalse();
}

