package com.divillafajar.app.pos.pos_app_sini.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TelegramNotifier {
    @Value("${app.telegram.bot-token}")
    private String botToken;

    @Value("${app.telegram.chat-id}")
    private String chatId;

    @Value("${app.telegram.env}")
    private String environment;

    @Value("${app.telegram.service}")
    private String serviceName;

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public void sendErrorAlert(Exception ex) {
        try {
            // 🕒 Waktu dan format
            String time = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // 🧩 Stack trace singkat (baris pertama saja)
            StackTraceElement top = ex.getStackTrace()[0];
            String topTrace = top.getClassName() + ":" + top.getLineNumber();

            // 🧠 Compose pesan Telegram
            String message = String.format("""
                    🚨 *[ERROR ALERT]*  
                    📅 *Time:* %s  
                    🌍 *Env:* %s  
                    🧱 *Service:* %s  
                    
                    ⚠️ *Exception:* `%s`  
                    🔍 *At:* `%s`
                    
                    _Auto sent by System Monitor_
                    """,
                    time, environment, serviceName,
                    ex.toString(), topTrace
            );

            sendMessage(message);
        } catch (Exception e) {
            System.err.println("⚠️ Gagal membuat pesan alert Telegram: " + e.getMessage());
        }
    }

    public void sendMessage(String text) {
        try {
            String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String url = String.format(
                    "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=Markdown",
                    botToken, chatId, encoded
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("✅ Telegram alert terkirim.");
            } else {
                System.err.println("⚠️ Gagal kirim alert. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            System.err.println("⚠️ Gagal kirim pesan Telegram: " + e.getMessage());
        }
    }
}
