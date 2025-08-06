package com.maps.mapservice.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.maps.mapservice.dto.MapResponse;
import com.maps.mapservice.util.StringMatcher;

import lombok.RequiredArgsConstructor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
public class PdfHighlightService {

    @Autowired
    private final StringMatcher matcher;


    // Method to highlight text and return image as byte array

   
    private byte[] highlightTextInPdf(String filePath, String searchText, List<Float> coordinates) throws IOException {
        String rawPath = filePath+"floor.pdf";
        if (rawPath.startsWith("\\") || rawPath.startsWith("/")) {
            rawPath = rawPath.substring(1);
        }

        // Normalize separators and split path
        String[] parts = rawPath.replace("\\", "/").split("/");

        // Convert to Path
        Path path = Paths.get(parts[0], java.util.Arrays.copyOfRange(parts, 1, parts.length));

        // Use FileReader
        
        PDDocument document = Loader.loadPDF(path.toFile());
        PDFTextStripper textStripper = new PDFTextStripper() {
            @Override
            protected void processTextPosition(TextPosition text) {
                super.processTextPosition(text);
                String result = matcher.appendCharacter(text.getUnicode().charAt(0), searchText);
                if (result != null) {
                    highlightWord(text, document, coordinates);
                }
            }

            private void highlightWord(TextPosition text, PDDocument document, List<Float> coordinates) {
                try {
                    PDPage page = getCurrentPage();
                    PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true,
                            true);

                    PDRectangle mediaBox = page.getMediaBox();
                    float pageHeight = mediaBox.getHeight();

                    float x = text.getXDirAdj();
                    float y = text.getYDirAdj();
                    float width = text.getWidthDirAdj();
                    float height = text.getHeightDir();

                    float correctedY = pageHeight - y;

                    coordinates.add(x);
                    coordinates.add(y);

                    contentStream.setNonStrokingColor(Color.RED);
                    contentStream.addRect(x, correctedY - height, width, height);
                    contentStream.fill();
                    contentStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        textStripper.setSortByPosition(true);
        textStripper.setStartPage(0);
        textStripper.setEndPage(document.getNumberOfPages());
        textStripper.getText(document);

        // Render the first page with highlight as image
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI(0, 700); // Only the first page

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        document.close();

        return baos.toByteArray(); // Return image bytes
    }

    private float convertPointsToPixels(float pointValue) {
        int dpi = 700;
        return pointValue * dpi / 72;
    }

    @Cacheable(value = "pdfCache", key = "#filePath + #searchText")
    public MapResponse getMapWithCoordinates(String filePath, String searchText) throws IOException {

        List<Float> coordinates = new ArrayList<>();
        System.out.println("File Path here is: " + filePath);
        byte[] imageBytes = highlightTextInPdf(filePath, searchText, coordinates);

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        List<Float> converted = coordinates.stream()
                .map(c -> convertPointsToPixels(c))
                .toList();


        return MapResponse.builder()
                .image(base64Image)
                .coordinates(converted)
                .build();
    }
}
