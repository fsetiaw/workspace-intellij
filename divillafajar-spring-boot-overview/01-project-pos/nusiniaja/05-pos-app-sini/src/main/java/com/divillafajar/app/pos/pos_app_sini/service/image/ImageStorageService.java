package com.divillafajar.app.pos.pos_app_sini.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageStorageService {
    /**
     * Simpan file ke lokal dan backup ke database
     */
    String saveProductImage(Long productId, MultipartFile file, String clientAddressPubId) throws IOException;

    /**
     * Ambil file untuk runtime (fallback ke DB bila tidak ada di lokal)
     */
    byte[] loadProductImage(Long productId) throws IOException;

    /**
     * Restore file yang hilang dari DB backup ke folder lokal
     */
    void restoreMissingImages() throws IOException;

    /**
     * Dapatkan path fisik file di folder lokal
     */
    Path getLocalPath(Long productId, String fileName);
}
