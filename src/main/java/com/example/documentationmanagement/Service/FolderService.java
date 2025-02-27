package com.example.documentationmanagement.Service;

import com.example.documentationmanagement.Repository.FolderRepository;
import com.example.documentationmanagement.entities.Folder;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FolderService {
    FolderRepository folderRepository;

    // Create a new folder
    public Folder createFolder(String folderName) {
        Folder folder = new Folder();
        folder.setName(folderName);
        return folderRepository.save(folder);
    }

    // Delete a folder (soft delete)
    public void deleteFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("Folder not found with ID: " + folderId));
        folder.setDeleted(true);
        folderRepository.save(folder);
    }

    // Get all folders (e.g., by project)
    public List<Folder> getFolders() {
        return folderRepository.findByIsDeletedFalse();
    }
}
