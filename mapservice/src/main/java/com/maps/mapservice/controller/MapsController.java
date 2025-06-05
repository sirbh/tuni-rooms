package com.maps.mapservice.controller;


import com.maps.mapservice.dto.HighlightRequest;
import com.maps.mapservice.dto.MapResponse;
import com.maps.mapservice.service.PdfHighlightService;

import lombok.RequiredArgsConstructor;


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
    @GetMapping("/getmap")
    public ResponseEntity<MapResponse> highlightText(@ModelAttribute HighlightRequest request) {
        String filePath = request.getLocation();
        String searchText = request.getRoom();
        System.out.println("File Path: " + filePath);
        try {


            MapResponse response = pdfHighlightService.getMapWithCoordinates(filePath, searchText);



            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
