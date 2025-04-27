// package com.maps.handler.util;

// import com.maps.handler.service.LocationCsvUploader;

// import lombok.RequiredArgsConstructor;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// @Component
// @RequiredArgsConstructor
// public class CsvUploadRunner implements CommandLineRunner {

//     @Autowired
//     private LocationCsvUploader locationCsvUploader;

//     @Override
//     public void run(String... args) throws Exception {
//         // You can pass the path here or get from args
//         String csvPath = "output.csv"; 

//         System.out.println("Starting CSV upload...");
//         locationCsvUploader.uploadCsv(csvPath);
//         System.out.println("CSV upload completed.");
//     }
// }
