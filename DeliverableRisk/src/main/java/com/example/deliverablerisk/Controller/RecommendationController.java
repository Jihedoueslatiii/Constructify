package com.example.deliverablerisk.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/python")
public class RecommendationController {
    @GetMapping("/run-recommendation")
    public ResponseEntity<String> runRecommendationScriptText() {
        try {
            String scriptPath = "C:\\Users\\Nadine Ziedi\\Downloads\\back_Nadinneeeeeeeeeeeee\\back\\recommandation.py";
            String pythonPath = "python";

            ProcessBuilder pb = new ProcessBuilder(pythonPath, scriptPath);
            pb.directory(new File("C:\\Users\\Nadine Ziedi\\Downloads\\back_Nadinneeeeeeeeeeeee\\back"));
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            );
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }

            int exitCode = process.waitFor();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN); // 👈 Affiche comme texte brut

            if (exitCode == 0) {
                return new ResponseEntity<>(output.toString(), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Erreur script Python:\n" + output.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (IOException | InterruptedException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>("Exception Java:\n" + e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
