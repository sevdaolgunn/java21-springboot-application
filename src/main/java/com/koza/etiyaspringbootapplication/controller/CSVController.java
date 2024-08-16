package com.koza.etiyaspringbootapplication.controller;

import com.koza.etiyaspringbootapplication.service.CSVService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/CSV")
public class CSVController {
    private final CSVService csvService;

    @PostMapping("/import")
    public ResponseEntity<String> importCSV(@RequestParam("tableName") String tableName,
                                            @RequestParam("file") MultipartFile file) {
        try {
            csvService.saveEntitiesFromCSV(tableName, file);
            return ResponseEntity.ok("CSV verileri başarıyla " + tableName + " tablosuna import edildi.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("İmport edilirken bir hata oluştu: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportEntitiesToCSV(
            @RequestParam("tableName") String tableName) {
        try {
            ByteArrayResource csvData = csvService.exportEntitiesToCSV(tableName);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tableName + ".csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource(("Export edilirken bir hata oluştu: " + e.getMessage()).getBytes()));
        }
    }
}
