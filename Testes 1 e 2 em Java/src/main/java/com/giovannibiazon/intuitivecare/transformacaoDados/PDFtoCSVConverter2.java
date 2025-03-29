package com.giovannibiazon.intuitivecare.transformacaoDados;

import com.opencsv.CSVWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PDFtoCSVConverter2 {
    public static void main(String[] args) {
        try {
            // 1. Extrair tabelas do PDF
            List<String[]> csvData = extractDataFromPDF("Anexo_I.pdf");

            // 2. Salvar CSV
            saveToCSV(csvData, "Rol_Procedimentos1.csv");

            // 3. Criar ZIP
            createZip("Rol_Procedimentos1.csv", "Teste_giovannibiazon1.zip");

            System.out.println("✅ CSV gerado com " + (csvData.size()-1) + " linhas de dados!");
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<String[]> extractDataFromPDF(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Código", "Descrição", "OD/AMB", "Porte", "Idade", "Vigência"});

        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String text = stripper.getText(document);

            // Debug: Exibir a saída completa para análise
            System.out.println("=== TEXTO EXTRAÍDO DO PDF ===");
            System.out.println(text);
            System.out.println("=============================");

            // Dividir em linhas e processar (apenas para teste, sem filtro por enquanto)
            String[] lines = text.split("\\r?\\n");
            for (String line : lines) {
                // Adicionar todas as linhas à lista para verificar se alguma tabela aparece
                data.add(new String[]{line});
            }
        }

        return data;
    }

    private static void saveToCSV(List<String[]> data, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
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
