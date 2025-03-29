package com.giovannibiazon.intuitivecare.webScraping;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String ANS_URL = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";

        try {
            System.out.println("🔍 Buscando PDFs no site da ANS...");

            // 1. Encontrar links
            List<String> pdfUrls = PdfDownloader.findPdfLinks(ANS_URL);
            if (pdfUrls.isEmpty()) {
                System.out.println("⚠️ Nenhum PDF encontrado!");
                return;
            }

            // 2. Baixar arquivos
            System.out.println("\n⬇️ Iniciando downloads...");
            List<String> downloadedFiles = PdfDownloader.downloadPdfs(pdfUrls, "downloads_ans");

            // 3. Compactar
            System.out.println("\n🗜 Criando arquivo ZIP...");
            ZipCreator.createZip(downloadedFiles, "anexos_ans.zip");

            System.out.println("\n✅ Processo concluído! 📦 Arquivo ZIP gerado com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }
}