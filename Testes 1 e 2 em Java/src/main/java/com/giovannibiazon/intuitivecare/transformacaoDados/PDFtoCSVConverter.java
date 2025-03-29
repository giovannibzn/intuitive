package com.giovannibiazon.intuitivecare.transformacaoDados;

import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;
import com.opencsv.CSVWriter;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class PDFtoCSVConverter {
    public static void main(String[] args) {
        try {
            // 1. Extrair tabelas do PDF
            List<String[]> csvData = extractTablesFromPDF("Anexo_I.pdf");

            // 2. Salvar CSV
            saveToCSV(csvData, "Rol_Procedimentos.csv");

            // 3. Criar ZIP
            createZip("Rol_Procedimentos.csv", "Teste_giovannibiazon.zip");

            System.out.println("✅ CSV gerado com " + (csvData.size() - 1) + " linhas de dados!");
        } catch (IOException e) {
            System.err.println("❌ Erro de E/S: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<String[]> extractTablesFromPDF(String filePath) throws IOException {
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{"Código", "Descrição", "OD/AMB", "Porte", "Idade", "Vigência"});

        try (PDDocument document = PDDocument.load(new File(filePath))) {
            ObjectExtractor extractor = new ObjectExtractor(document);
            SpreadsheetExtractionAlgorithm algorithm = new SpreadsheetExtractionAlgorithm();
            PageIterator pages = extractor.extract();

            while (pages.hasNext()) {
                Page page = pages.next();
                List<Table> tables = algorithm.extract(page);

                for (Table table : tables) {
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        List<String> rowData = new ArrayList<>();
                        for (RectangularTextContainer cell : row) {
                            rowData.add(cell.getText().trim());
                        }
                        csvData.add(rowData.toArray(new String[0]));
                    }
                }
            }
        }

        return csvData;
    }

    private static void saveToCSV(List<String[]> data, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(data);
        }
    }

    private static void createZip(String sourceFile, String zipFile) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
             FileInputStream fis = new FileInputStream(sourceFile)) {
            zos.putNextEntry(new ZipEntry(sourceFile));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
        }
    }
}