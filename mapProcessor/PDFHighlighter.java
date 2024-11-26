import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.awt.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
public class PDFHighlighter {

    static List<Float> coordinates = null;

    public static List<Float> getMap(String pdfLocation, String searchString) {
        String inputFilePath = pdfLocation.replace("\\", "/"); // Path to the input PDF
        String outputFilePath = "output.pdf"; // Path to save the output PDF
        String searchText = searchString.trim(); // Text to search and highlight

        try {
            highlightTextInPDF(inputFilePath, outputFilePath, searchText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    private static void highlightTextInPDF(String inputFilePath, String outputFilePath, String searchText)
            throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputFilePath));
        StringMatcher matcher = new StringMatcher();
        PDFTextStripper textStripper = new PDFTextStripper() {
            // private StringBuilder currentWord = new StringBuilder(); // To accumulate characters into words

            @Override
            protected void processTextPosition(TextPosition text) {
                super.processTextPosition(text);
                // currentWord.append(text.getUnicode()); // Accumulate characters
                // System.out.println("Current word: " + currentWord);
                String result = matcher.appendCharacter(text.getUnicode().charAt(0), searchText);
                if (result != null) {
                    System.out.println("Result: " + result);
                    System.out.println("Target matched: " + result);
                    highlightWord(text, document);
                    System.out.println("Found: " + text.getX());
                    return;// Stop after finding the target
                }
            }

            private void highlightWord(TextPosition text, PDDocument document) {
                try {
                    PDPage page = getCurrentPage();
                    PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true,
                            true);

                    PDRectangle mediaBox = page.getMediaBox(); // Get the page's media box (dimensions)
                    float pageHeight = mediaBox.getHeight(); // Get the height of the page

                    float x = text.getXDirAdj(); // X coordinate is correct
                    float y = text.getYDirAdj(); // Y coordinate needs adjustment
                    float width = text.getWidthDirAdj(); // Width of the text
                    float height = text.getHeightDir(); // Height of the text

                    // Invert the Y-coordinate for PDF's bottom-left origin system
                    float correctedY = pageHeight - y;

                    System.err.println("Original Y: " + y + ", Corrected Y: " + correctedY);
                    System.err.println("Original Y: " + x + ", Corrected Y: " + x);

                    // coordinates.add(x);
                    // coordinates.add(y);

                    coordinates = new ArrayList<Float>();
                    coordinates.add(x);
                    coordinates.add(y);

                    // Set the color for highlighting
                    contentStream.setNonStrokingColor(Color.RED);
                    contentStream.addRect(x, correctedY - height, width, height); // Corrected Y and its height
                                                                                  // adjustment
                    contentStream.fill();
                    contentStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        // Process the PDF and search for text
        textStripper.setSortByPosition(true);
        textStripper.setStartPage(0);
        textStripper.setEndPage(document.getNumberOfPages());
        textStripper.getText(document);

        PDFRenderer renderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage image = renderer.renderImageWithDPI(page, 700); // Render at 300 DPI for better quality
            File outputFile = new File("output_page_" + (page + 1) + ".jpeg");
            ImageIO.write(image, "jpeg", outputFile);
        }

        // Save the modified PDF
        // document.save(outputFilePath);
        document.close();
    }
}
