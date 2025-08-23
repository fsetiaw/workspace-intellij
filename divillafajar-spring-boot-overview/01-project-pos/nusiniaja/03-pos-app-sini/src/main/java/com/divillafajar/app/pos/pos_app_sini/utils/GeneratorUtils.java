package com.divillafajar.app.pos.pos_app_sini.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Component
public class GeneratorUtils {
    private final MyStringUtils myStringUtils;
    public GeneratorUtils(MyStringUtils myStringUtils) {
        this.myStringUtils=myStringUtils;
    }

    public String generatePubUserId(int length) {
        return myStringUtils.generateRandomString(length);
    }

    public static BufferedImage generateQRCodeImage(String text, int width, int height, File logoFile) throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height
        );

        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig());

        if (logoFile != null && logoFile.exists()) {
            BufferedImage logo = ImageIO.read(logoFile);

            int logoWidth = qrImage.getWidth() / 5;
            int logoHeight = qrImage.getHeight() / 5;
            int logoX = (qrImage.getWidth() - logoWidth) / 2;
            int logoY = (qrImage.getHeight() - logoHeight) / 2;

            Graphics2D g = qrImage.createGraphics();
            g.drawImage(logo, logoX, logoY, logoWidth, logoHeight, null);
            g.dispose();
        }

        return qrImage;
    }
}

