package com.giovannibiazon.intuitivecare.webScraping;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.zip.*;

public class ZipCreator {

    public static void createZip(List<String> files, String zipName) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipName))) {
            System.out.println("\n🔨 Iniciando criação do ZIP...");

            for (String file : files) {
                addToZip(zos, file);
                System.out.println("📄 Adicionado: " + file);
            }

            System.out.println("✅ ZIP criado com sucesso: " + zipName);
        }

        cleanTempFiles(files); // Limpeza após criação
    }

    private static void addToZip(ZipOutputStream zos, String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    private static void cleanTempFiles(List<String> files) {
        System.out.println("\n🧹 LIMPEZA DE ARQUIVOS TEMPORÁRIOS");
        System.out.println("──────────────────────────────────");

        int deletedFiles = 0;
        for (String file : files) {
            try {
                Path path = Paths.get(file);
                if (Files.exists(path)) {
                    Files.delete(path);
                    System.out.println("🗑️ Removido: " + path.getFileName());
                    deletedFiles++;
                }
            } catch (IOException e) {
                System.err.println("⚠️ Erro ao remover " + file + ": " + e.getMessage());
            }
        }

        System.out.println("──────────────────────────────────");
        System.out.println("♻️ Total de arquivos removidos: " + deletedFiles + "/" + files.size());
        System.out.println("✨ Pasta temporária limpa com sucesso!\n");
    }
}