package com.maps.handler.service;

import com.maps.handler.model.Location;
import com.maps.handler.repository.LocationRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class LocationCsvUploader {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationCsvUploader(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public void uploadCsv(InputStream csvInputStream) {
        try (
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(csvInputStream));
            CSVReader reader = new CSVReaderBuilder(bufferedReader)
                .withCSVParser(new CSVParserBuilder().withSeparator(',').withEscapeChar('~').build())
                .build()
        ) {
            List<String[]> lines = reader.readAll();
            boolean isFirstLine = true;

            for (String[] parts : lines) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                if (parts.length >= 2) {
                    String room = parts[0].trim();
                    String fileLocation = parts[1].trim();

                    Location location = new Location();
                    location.setRoom(room);
                    location.setFileLocation(fileLocation);
                    locationRepository.save(location);
                }
            }
        } catch (Exception e) {
            System.err.println("Error while uploading CSV:");
            e.printStackTrace();
        }
    }
}
