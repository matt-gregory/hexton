package com.rarelittlebeastie.onixweb.onixApp.utils;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class FileUtils {
    public byte[] readFile(File file) throws IOException {
        byte[] buffer = new byte[8192];
        int read;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file)) {
            while ((read = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }
            byteArrayOutputStream.flush();
            buffer = byteArrayOutputStream.toByteArray();
        }
        return buffer;
    }
}
