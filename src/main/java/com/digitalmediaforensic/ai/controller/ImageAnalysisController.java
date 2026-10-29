
package com.digitalmediaforensic.ai.controller;

import com.digitalmediaforensic.ai.service.PythonAiService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageAnalysisController {

    private final PythonAiService pythonAiService;

    public ImageAnalysisController(PythonAiService pythonAiService) {
        this.pythonAiService = pythonAiService;
    }

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> analyzeImage(
            @RequestParam("file") MultipartFile file) throws Exception {
        System.out.println("Image endpoint called!");
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"Please upload an image\"}");
        }

        String filename = file.getOriginalFilename();

        if (filename == null || filename.isBlank()) {
            filename = "upload.png";
        }
        System.out.println("Filename is: " + filename);
        String result = pythonAiService.analyzeImage(
                file.getBytes(), filename);
        System.out.println("Result is: " + result);
        return ResponseEntity.ok(result);
    }
}

