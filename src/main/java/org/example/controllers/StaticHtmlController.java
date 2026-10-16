package org.example.controllers;

import org.example.annotation.Cumponent;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Cumponent
@RestController
public class StaticHtmlController {

    private static final String HTML_PAGE = """
            <!DOCTYPE html>
            <html lang="ru">
            <head>
                <meta charset="UTF-8">
                <title>Редакция новостного издания</title>
            </head>
            <body>
                <h1>Редакция новостного издания</h1>
                <p>Запрос успешно обработан.</p>
            </body>
            </html>
            """;

    @PostMapping(
            value = "/api/static-html",
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> handlePost() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(HTML_PAGE);
    }

    @PutMapping(
            value = "/api/static-html",
            produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> handlePut() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(HTML_PAGE);
    }
}