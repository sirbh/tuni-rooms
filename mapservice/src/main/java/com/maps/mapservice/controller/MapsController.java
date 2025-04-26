package com.maps.mapservice.controller;

import com.maps.mapservice.dto.HighlightRequest;
import com.maps.mapservice.service.PdfHighlightService;

import lombok.RequiredArgsConstructor;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MapsController {

    private final PdfHighlightService pdfHighlightService;


    // Endpoint to highlight text and return an image
    @GetMapping("/highlight")
    public ResponseEntity<Map<String, String>> highlightText(@ModelAttribute HighlightRequest request) {
        String filePath = request.getLocation();
        String searchText = request.getRoom();
        try {
            // Call service method to highlight text and get the image as byte array
            byte[] imageBytes = pdfHighlightService.highlightTextInPdf(filePath, searchText);

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Construct the response map
            Map<String, String> response = new HashMap<>();
            response.put("image", base64Image);

            response.put("coordinates", pdfHighlightService.getCoordinates().stream().map(c-> pdfHighlightService.convertPointsToPixels((c))).toList().toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
