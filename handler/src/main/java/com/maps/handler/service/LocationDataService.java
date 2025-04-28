package com.maps.handler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.maps.handler.exception.ResourceNotFoundException;
import com.maps.handler.model.Location;
import com.maps.handler.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationDataService {
   
    @Autowired
    private final LocationRepository locationRepository;

    public List<Location> serachByRoom(String room) {
       List<Location> list =  locationRepository.findByRoomContainingIgnoreCase(room, PageRequest.of(0, 10)); 
       if(list.isEmpty()) {
           throw ResourceNotFoundException.builder()
                   .resourceName("Location")
                   .fieldName("room")
                   .fieldValue(room)
                   .build();
       } else {
           return list;
       }
    }
}
