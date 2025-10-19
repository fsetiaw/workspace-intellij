package com.divillafajar.app.pos.pos_app_sini.utils;

import org.springframework.stereotype.Component;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class FileCleaner {
    public void cleanupOldProductImages(
            String clientAddressPubId,
            Path uploadPath,
            Path thumbPath,
            Path trashBaseDir,
            String prefixBaseName,
            String latestFileName,
            String latestThumbFileName
    ) {
        try {
            // 🔹 Buat folder trash berdasarkan productId
            System.out.println("uploadPath="+uploadPath);
            System.out.println("thumbPath="+thumbPath);
            System.out.println("trashBaseDir="+trashBaseDir);
            System.out.println("prefixBase="+prefixBaseName);
            System.out.println("latestFileName="+latestFileName);
            System.out.println("latestThumbFileName="+latestThumbFileName);
            System.out.println("latestThumbFileName="+latestThumbFileName);
            String productIdPart = prefixBaseName.replace("product_", "").replaceAll("_$", "");
            Path productTrashDir = trashBaseDir.resolve(clientAddressPubId).resolve("product").resolve(productIdPart);
            Files.createDirectories(productTrashDir);

            // 🔹 Pindahkan file utama lama
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadPath, prefixBaseName + "*")) {
                for (Path path : stream) {
                    String name = path.getFileName().toString();
                    if (!name.equals(latestFileName)) {
                        Path target = productTrashDir.resolve(name);
                        System.out.println("target path = "+target);
                        Files.move(path, target, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("🗑️ Pindahkan file lama ke trash: " + name);
                    }
                }
            }

            // 🔹 Pindahkan thumbnail lama
            try (DirectoryStream<Path> thumbStream = Files.newDirectoryStream(thumbPath, prefixBaseName + "*")) {
                for (Path path : thumbStream) {
                    String name = path.getFileName().toString();
                    if (!name.equals(latestThumbFileName)) {
                        Path target = productTrashDir.resolve(name);
                        Files.move(path, target, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("🗑️ Pindahkan thumbnail lama ke trash: " + name);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("⚠️ Gagal memindahkan file lama ke trash: " + e.getMessage());
        }
    }

}
