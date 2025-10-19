package com.divillafajar.app.pos.pos_app_sini.service.image;

import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.product.ProductRepo;
import com.divillafajar.app.pos.pos_app_sini.utils.FileCleaner;
import com.divillafajar.app.pos.pos_app_sini.utils.MyStringUtils;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageStorageServiceImpl implements ImageStorageService{
    private final ProductRepo productRepo;
    private final FileCleaner fileCleaner;
    private final MyStringUtils stringUtils;

    // Bisa diatur dari application.properties
    @Value("${app.upload.image-dir}")
    private String imageBaseDir;

    @Value("${app.upload.image-base-url}")
    private String imageBaseUrl;

    @Value("${app.upload.bin-dir}")
    private String trashBinDir;


    @Override
    @Transactional
    public String saveProductImage(Long productId, MultipartFile file, String clientAddressPubId) throws IOException {
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        // 🔹 Buat nama file unik
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String prefixBaseName = "product_" + productId + "_";
        String baseName = prefixBaseName + System.currentTimeMillis();
        String fileExtension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = baseName + "_" + originalFilename.substring(0, dotIndex);
            fileExtension = originalFilename.substring(dotIndex);
        }

        String fileName = baseName + fileExtension;
        String thumbFileName = baseName + "_thumb" + fileExtension;

        // 🔹 Buat base URL path
        String targetDir = "product";
        String baseUrlPath = imageBaseUrl + "/" + clientAddressPubId + "/" + targetDir + "/";

        // 🔹 Potong nama file agar total URL <= 255
        int maxLength = 235;
        fileName = stringUtils.adjustFileNameLength(baseUrlPath, fileName, maxLength);
        thumbFileName = stringUtils.adjustFileNameLength(baseUrlPath + "thumbs/", thumbFileName, maxLength);

        // 🔹 Simpan metadata ke DB
        product.setImageUrl(baseUrlPath + fileName);
        product.setThumbnailUrl(baseUrlPath + "thumbs/" + thumbFileName);
        product.setImageType(file.getContentType());
        product.setImageBackup(file.getBytes());

        // 🔹 Path upload
        Path uploadPath = Paths.get(imageBaseDir, clientAddressPubId, targetDir).toAbsolutePath().normalize();
        Path thumbPath = uploadPath.resolve("thumbs");
        Path trashBin = Paths.get(trashBinDir).toAbsolutePath().normalize();

        Files.createDirectories(uploadPath);
        Files.createDirectories(thumbPath);
        Files.createDirectories(trashBin);

        // 🔹 Simpan file utama
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // ==========================
        // 🔹 Generate thumbnail (auto crop 16:9)
        // ==========================
        Path thumbFilePath = thumbPath.resolve(thumbFileName);
        BufferedImage src = ImageIO.read(filePath.toFile());

        int width = src.getWidth();
        int height = src.getHeight();

        // Tentukan rasio target 16:9
        double targetRatio = 16.0 / 9.0;
        int targetWidth = width;
        int targetHeight = (int) (width / targetRatio);

        // Jika tinggi hasil crop > tinggi asli → sesuaikan ke tinggi
        if (targetHeight > height) {
            targetHeight = height;
            targetWidth = (int) (height * targetRatio);
        }

        // Hitung posisi crop dari tengah
        int x = (width - targetWidth) / 2;
        int y = (height - targetHeight) / 2;

        // Crop dan resize ke ukuran thumbnail (misal 800×450)
        Thumbnails.of(src)
                .sourceRegion(x, y, targetWidth, targetHeight)
                .size(800, 450)
                .outputQuality(0.85)
                .toFile(thumbFilePath.toFile());

        // ==========================

        productRepo.save(product);

        System.out.println("✅ Gambar & thumbnail berhasil disimpan:");
        System.out.println(" - Image URL: " + product.getImageUrl());
        System.out.println(" - Thumbnail URL: " + product.getThumbnailUrl());

        // 🔹 Bersihkan file lama
        fileCleaner.cleanupOldProductImages(
                clientAddressPubId, uploadPath, thumbPath, trashBin,
                prefixBaseName, fileName, thumbFileName
        );

        return product.getThumbnailUrl();
    }

    /*
    public String saveProductImage(Long productId, MultipartFile file, String clientAddressPubId) throws IOException {
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        // 🔹 Buat nama file unik
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String prefixBaseName = "product_" + productId + "_";
        String baseName = prefixBaseName + System.currentTimeMillis();
        String fileExtension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = baseName + "_" + originalFilename.substring(0, dotIndex);
            fileExtension = originalFilename.substring(dotIndex);
        }

        String fileName = baseName + fileExtension;
        String thumbFileName = baseName + "_thumb" + fileExtension;

        // 🔹 Buat base URL path
        String targetDir = "product";
        String baseUrlPath = imageBaseUrl + "/" + clientAddressPubId + "/" + targetDir + "/";

        // 🔹 Potong nama file agar total URL <= 255
        int maxLength = 235;
        fileName = stringUtils.adjustFileNameLength(baseUrlPath, fileName, maxLength);
        thumbFileName = stringUtils.adjustFileNameLength(baseUrlPath + "thumbs/", thumbFileName, maxLength);

        // 🔹 Simpan metadata ke DB lebih dulu
        product.setImageUrl(baseUrlPath + fileName);
        product.setThumbnailUrl(baseUrlPath + "thumbs/" + thumbFileName);
        product.setImageType(file.getContentType());
        product.setImageBackup(file.getBytes());

        // 🔹 Path upload
        Path uploadPath = Paths.get(imageBaseDir, clientAddressPubId, targetDir).toAbsolutePath().normalize();
        Path thumbPath = uploadPath.resolve("thumbs");
        Path trashBin = Paths.get(trashBinDir).toAbsolutePath().normalize();

        Files.createDirectories(uploadPath);
        System.out.println("uploadPath==="+uploadPath);
        Files.createDirectories(thumbPath);
        System.out.println("thumbPath==="+thumbPath);
        Files.createDirectories(trashBin);

        // 🔹 Simpan file utama
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 🔹 Generate thumbnail pakai Thumbnailator
        Path thumbFilePath = thumbPath.resolve(thumbFileName);
        Thumbnails.of(filePath.toFile())
                .size(300, 300)
                .outputQuality(0.8)
                .toFile(thumbFilePath.toFile());

        productRepo.save(product);

        System.out.println("✅ Gambar & thumbnail berhasil disimpan:");
        System.out.println(" - Image URL: " + product.getImageUrl());
        System.out.println(" - Thumbnail URL: " + product.getThumbnailUrl());

        // 🔹 Pindahkan file lama ke trash
        fileCleaner.cleanupOldProductImages(clientAddressPubId, uploadPath, thumbPath, trashBin, prefixBaseName, fileName, thumbFileName);
        return product.getThumbnailUrl();
    }

     */



    @Override
    public byte[] loadProductImage(Long productId) throws IOException {
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        if (product.getImageUrl() != null) {
            Path filePath = Paths.get(product.getImageUrl().replaceFirst("^/", ""));
            if (Files.exists(filePath)) {
                return Files.readAllBytes(filePath);
            }
        }

        // fallback ke DB
        if (product.getImageBackup() != null) {
            return product.getImageBackup();
        }

        throw new IOException("Gambar tidak ditemukan (lokal dan DB kosong)");
    }

    @Override
    public void restoreMissingImages() throws IOException {
        List<ProductEntity> products = productRepo.findAll();

        for (ProductEntity product : products) {
            if (product.getImageUrl() == null || product.getImageBackup() == null) continue;

            Path localPath = Paths.get(product.getImageUrl().replaceFirst("^/", ""));
            if (!Files.exists(localPath) && product.getImageBackup() != null) {
                Files.createDirectories(localPath.getParent());
                Files.write(localPath, product.getImageBackup(), StandardOpenOption.CREATE);
                System.out.println("✅ Restored missing image for: " + product.getName());
            }
        }
    }

    @Override
    public Path getLocalPath(Long productId, String fileName) {
        return Paths.get(imageBaseDir).resolve(productId + "_" + fileName).toAbsolutePath();
    }
}
