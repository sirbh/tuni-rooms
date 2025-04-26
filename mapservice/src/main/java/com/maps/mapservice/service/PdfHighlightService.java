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

import com.maps.mapservice.util.StringMatcher;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
public class PdfHighlightService {

    @Autowired
    private final StringMatcher matcher;

    private ArrayList<Float> coordinates = new ArrayList<>(); // Store coordinates for future use

    // Method to highlight text and return image as byte array

    @Cacheable(value = "pdfCache", key = "#filePath + #searchText")
    public byte[] highlightTextInPdf(String filePath, String searchText) throws IOException {
        PDDocument document = Loader.loadPDF(new File(filePath));
        PDFTextStripper textStripper = new PDFTextStripper() {
            @Override
            protected void processTextPosition(TextPosition text) {
                super.processTextPosition(text);
                String result = matcher.appendCharacter(text.getUnicode().charAt(0), searchText);
                if (result != null) {
                    highlightWord(text, document);
                }
            }

            private void highlightWord(TextPosition text, PDDocument document) {
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

                    coordinates = new ArrayList<>();
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

    public float convertPointsToPixels(float pointValue) {
        int dpi = 700;
        return pointValue * dpi / 72;
    }

    public ArrayList<Float> getCoordinates() {
        return coordinates; 
    }
}
