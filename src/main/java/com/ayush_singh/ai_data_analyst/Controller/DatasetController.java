package com.ayush_singh.ai_data_analyst.Controller;

import com.ayush_singh.ai_data_analyst.Model.DataSetResponse;
import com.ayush_singh.ai_data_analyst.Service.CsvService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/dataset")
public class DatasetController {
    private final CsvService csvService;

    DatasetController(CsvService csvService) {
        this.csvService = csvService;
    }
    @PostMapping("/upload")
    public ResponseEntity<DataSetResponse> upload(@RequestParam("file")MultipartFile file){
        DataSetResponse response = csvService.upload(file);
        return ResponseEntity.ok(response);
    }


}
