package com.maps.handler.contoller;

import java.util.List;

import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maps.handler.model.Location;
import com.maps.handler.service.LocationDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LocationController {
     
    @Autowired
    private final LocationDataService locationDataService;

    @GetMapping("/search")
    public List<Location> searchByRoom(@RequestParam String room) {

        System.out.println("Searching for room: " + room);
        return locationDataService.serachByRoom(room);
    }
    
}
