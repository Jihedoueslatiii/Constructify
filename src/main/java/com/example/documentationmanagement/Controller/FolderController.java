package com.example.documentationmanagement.Controller;

import com.example.documentationmanagement.Service.FolderService;
import com.example.documentationmanagement.entities.Folder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("folders")
public class FolderController {
    FolderService folderService;

    // Create a folder
    @PostMapping("/create")
    public ResponseEntity<Folder> createFolder(@RequestParam("folderName") String folderName) {
        Folder folder = folderService.createFolder(folderName);
        return new ResponseEntity<>(folder, HttpStatus.CREATED);
    }

    // Delete a folder (soft delete)
    @DeleteMapping("/delete/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId) {
        folderService.deleteFolder(folderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all folders
    @GetMapping("/all")
    public ResponseEntity<List<Folder>> getAllFolders() {
        List<Folder> folders = folderService.getFolders();
        return new ResponseEntity<>(folders, HttpStatus.OK);
    }
}
