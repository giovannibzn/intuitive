package com.giovannibiazon.intuitivecare.webScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class PdfDownloader {

    public static List<String> findPdfLinks(String url) throws IOException {
        System.out.println("🌐 Conectando ao site...");
        List<String> pdfLinks = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        for (Element link : doc.select("a[href$=.pdf]")) {
            String href = link.attr("abs:href");
            if (href.matches(".*Anexo[ _]?I+.*")) {
                pdfLinks.add(href);
                System.out.println("🔗 Link encontrado: " + href);
            }
        }
        return pdfLinks;
    }

    public static List<String> downloadPdfs(List<String> urls, String outputDir) throws IOException {
        Files.createDirectories(Paths.get(outputDir));
        List<String> downloadedFiles = new ArrayList<>();

        for (String url : urls) {
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            String filePath = outputDir + File.separator + fileName;

            System.out.println("\n📥 Baixando: " + fileName);
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                downloadedFiles.add(filePath);
                System.out.println("✔️ Salvo em: " + filePath);
            } catch (IOException e) {
                System.err.println("⚠️ Falha no download: " + e.getMessage());
            }
        }
        return downloadedFiles;
    }
}