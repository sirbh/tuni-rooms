package com.maps.handler.service;

import java.util.List;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.maps.handler.model.Location;
import com.maps.handler.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationDataService {
   
    @Autowired
    private final LocationRepository locationRepository;

    public List<Location> serachByRoom(String line) {
        return locationRepository.findByRoomContainingIgnoreCase(line, PageRequest.of(0, 10)); 
    }
}
