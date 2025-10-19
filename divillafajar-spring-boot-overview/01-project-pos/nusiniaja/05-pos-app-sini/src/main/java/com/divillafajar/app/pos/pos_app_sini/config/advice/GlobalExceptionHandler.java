package com.divillafajar.app.pos.pos_app_sini.config.advice;

import com.divillafajar.app.pos.pos_app_sini.utils.TelegramNotifier;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final TelegramNotifier telegramNotifier;

    public GlobalExceptionHandler(TelegramNotifier telegramNotifier) {
        this.telegramNotifier = telegramNotifier;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedError(Exception ex) {
        // 📨 Kirim ke Telegram
        telegramNotifier.sendErrorAlert(ex);

        // 🧾 Log ke console/server
        ex.printStackTrace();

        // 🔙 Balikkan respon error ke client
        return ResponseEntity.internalServerError().body("Terjadi kesalahan internal. Tim teknis telah mendapat notifikasi.");
    }
}