package com.maps.handler.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.maps.handler.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByRoomContainingIgnoreCase(String line, Pageable pageable); // Use the correct type for 'line'
}
