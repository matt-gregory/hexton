package com.rarelittlebeastie.onixweb.onixApp.controller;

import com.rarelittlebeastie.onixweb.onixApp.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/images")
@Slf4j
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/small/{isbn}.jpg")
    public ResponseEntity<byte[]> getSmallImage(@PathVariable String isbn) {
        return getImage(isbn, false);
    }

    @GetMapping("/large/{isbn}.jpg")
    public ResponseEntity<byte[]> getLargeImage(@PathVariable String isbn) {
        return getImage(isbn, true);
    }

    private ResponseEntity<byte[]> getImage(String isbn, boolean large) {
        byte[] media;
        try {
            media = large ? imageService.getLargeImage(isbn) : imageService.getSmallImage(isbn);
        } catch (IOException e) {
            log.info("caught this", e);
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).getHeaderValue());
        headers.add("Content-Type", "image/jpg");
        headers.add("Content-Length", String.valueOf(media.length));
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }
}