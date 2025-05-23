package com.example.inovaTest.services;

import com.google.firebase.cloud.StorageClient;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class FirebaseStorageService {

    public void deleteFile(String fileUrl) {
        try {
            // Extrair o caminho relativo da URL
            String filePath = extractFilePathFromUrl(fileUrl);

            // Obter o bucket
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(filePath);

            if (blob == null) {
                throw new RuntimeException("Arquivo não encontrado no Firebase Storage: " + filePath);
            }

            boolean deleted = blob.delete();

            if (!deleted) {
                throw new RuntimeException("Falha ao deletar o arquivo: " + filePath);
            }

            System.out.println("Arquivo deletado com sucesso: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo no Firebase Storage: " + fileUrl, e);
        }
    }

    private String extractFilePathFromUrl(String fileUrl) {
        // Extrai o caminho após '/o/' e antes de '?'
        String path = fileUrl.split("/o/")[1].split("\\?")[0];
        // Decodifica caracteres especiais (ex.: %2F para /)
        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }
}