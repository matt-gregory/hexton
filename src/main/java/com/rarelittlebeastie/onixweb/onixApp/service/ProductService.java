package com.rarelittlebeastie.onixweb.onixApp.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.rarelittlebeastie.onixweb.onixApp.model.Products;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
@Slf4j
public class ProductService {


    @Value("${onixFile:/Users/matt/workspace/onixreader/temp/ONIX/pushmepress-ONIX.xml}")
    private String fileLocation;

    private XmlMapper xmlMapper;

    private long timestamp = 0;

    @Getter
    private Products products;

    public ProductService() {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        xmlMapper = new XmlMapper(module);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduledRead() {
        try {
            readProducts();
        } catch (IOException e) {
            log.error("caught this", e);
        }
    }

    public void readProducts() throws IOException {
        log.debug("Reading products");
        File file = new File(fileLocation);
        if (!file.exists()) {
            log.error("Couldn't read product file from {}", file.getAbsolutePath());
            return;
        }
        long timestamp = file.lastModified();
        if (this.timestamp == timestamp) {
            log.debug("File hasn't changed");
            return;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Products products = xmlMapper.readValue(fileInputStream, Products.class);
            this.products = products;
            this.timestamp = timestamp;
        } finally {
            if (this.products == null) {
                log.warn("products is null");
            } else if (this.products.getProducts().isEmpty()) {
                log.warn("products list is empty");
            } else {
                log.info("read {} products", this.products.getProducts().size());
            }
        }
    }
}
