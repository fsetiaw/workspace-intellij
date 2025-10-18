package com.divillafajar.app.pos.pos_app_sini.service.image;

import com.divillafajar.app.pos.pos_app_sini.io.entity.product.ProductEntity;
import com.divillafajar.app.pos.pos_app_sini.repo.product.ProductRepo;
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

    // Bisa diatur dari application.properties
    @Value("${app.upload.image-dir}")
    private String imageBaseDir;

    @Value("${app.upload.image-base-url}")
    private String imageBaseUrl;

    @Override
    @Transactional
    public void saveProductImage(Long productId, MultipartFile file) throws IOException {
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        try {
            // 🔹 Buat nama file unik
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String baseName = "product_" + productId + "_" + System.currentTimeMillis();
            String fileExtension = "";

            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }

            String fileName = baseName + fileExtension;
            String thumbFileName = baseName + "_thumb" + fileExtension;

            // 🔹 Path folder utama dan thumbnail
            String targetDir = "product";
            Path uploadPath = Paths.get(imageBaseDir, targetDir).toAbsolutePath().normalize();
            Path thumbPath = uploadPath.resolve("thumbs");

            Files.createDirectories(uploadPath);
            Files.createDirectories(thumbPath);

            // 🔹 Simpan file utama
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 🔹 Generate thumbnail pakai Thumbnailator
            Path thumbFilePath = thumbPath.resolve(thumbFileName);
            Thumbnails.of(filePath.toFile())
                    .size(300, 300) // ubah ukuran sesuai kebutuhan
                    .outputQuality(0.8)
                    .toFile(thumbFilePath.toFile());

            // 🔹 Simpan metadata ke DB
            product.setImageBackup(file.getBytes());
            product.setImageType(file.getContentType());
            product.setImageUrl(imageBaseUrl + "/" + targetDir + "/" + fileName);
            product.setThumbnailUrl(imageBaseUrl + "/" + targetDir + "/thumbs/" + thumbFileName);

            productRepo.save(product);

            System.out.println("✅ Gambar dan thumbnail berhasil disimpan:");
            System.out.println(" - Image URL: " + product.getImageUrl());
            System.out.println(" - Thumbnail URL: " + product.getThumbnailUrl());

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Gagal menyimpan gambar produk", e);
        }
    }


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
