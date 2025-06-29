package com.maps.handler.util;

import com.maps.handler.service.LocationCsvUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class CsvUploadRunner implements CommandLineRunner {

    private final LocationCsvUploader locationCsvUploader;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting CSV upload...");

        // Load the file from classpath (e.g., src/main/resources/u_output.csv)
        try (InputStream csvStream = getClass().getClassLoader().getResourceAsStream("u_output.csv")) {
            if (csvStream == null) {
                throw new RuntimeException("❌ u_output.csv not found in classpath!");
            }

            locationCsvUploader.uploadCsv(csvStream);
            System.out.println("✅ CSV upload completed.");
        } catch (Exception e) {
            System.err.println("❌ Error during CSV upload:");
            e.printStackTrace();
        }
    }
}
