package com.maps.handler.contoller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maps.handler.dto.SearchRequest;
import com.maps.handler.model.Location;
import com.maps.handler.service.LocationDataService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LocationController {
     
    @Autowired
    private final LocationDataService locationDataService;

    @GetMapping("/search")
    public List<Location> searchByRoom(@Valid @ModelAttribute SearchRequest searchRequest) {

        System.out.println("Searching for room: " + searchRequest.getRoom());
        return locationDataService.serachByRoom(searchRequest.getRoom());
    }
    
}
