package com.rarelittlebeastie.onixweb.onixApp.service;

import com.rarelittlebeastie.onixweb.onixApp.model.ImageData;
import com.rarelittlebeastie.onixweb.onixApp.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

@Service
@Slf4j
public class ImageService {

    static final String[] MAGICK_CANDIDATES = {"/usr/bin/convert", "/usr/local/bin/convert"};

    @Value("${imageUploadDir:/Users/matt/workspace/onixreader/temp/images}")
    private String imgUploadLocation;

    @Value("${imgProcessDir:~}")
    private String imgProcessDir;

    private static final String IMG_LOCATION_LARGE = "/images/large";
    private static final String IMG_LOCATION_SMALL = "/images/small";
    private static final String IMG_NOT_AVAILABLE_LARGE = "/largeNa.jpg";
    private static final String IMG_NOT_AVAILABLE_SMALL = "/smallNa.jpg";
    private static final int IMG_HEIGHT_LARGE = 1024;
    private static final int IMG_HEIGHT_SMALL = 200;

    @Autowired
    private FileUtils fileUtils;

    private Map<String, ImageData> imageDataMap = new HashMap<>();

    public byte[] getLargeImage(String isbn) throws IOException {
        return getImage(isbn, true);
    }

    public byte[] getSmallImage(String isbn) throws IOException {
        return getImage(isbn, false);
    }

    private byte[] getImage(String isbn, boolean large) throws IOException {
        ImageData imageData = imageDataMap.get(isbn);
        if (imageData == null || imageOutdated(imageData)) {
            loadImages(isbn);
        }
        File dir = new File(imgProcessDir + (large ? IMG_LOCATION_LARGE : IMG_LOCATION_SMALL));
        File img = new File(dir, isbn + ".jpg");
        return fileUtils.readFile(img);
    }

    private boolean imageOutdated(ImageData imageData) throws FileNotFoundException {
        File img = getImageFile(imageData.getIsbn());
        if (img.lastModified() != imageData.getTimestamp()) {
            log.debug("image for {} is outdated will reload", imageData.getTimestamp());
            return true;
        }
        return false;
    }

    private void loadImages(String isbn) throws IOException {
        File imgFile;
        try{
            imgFile=getImageFile(isbn);
        } catch(FileNotFoundException fileNotFoundException){
            imgFile=getImageFile("na");
        }
        BufferedImage bufferedImage = ImageIO.read(imgFile);

        File smallImgDir = new File(imgProcessDir + IMG_LOCATION_SMALL);
        if (!smallImgDir.exists() && !smallImgDir.mkdirs()) {
            throw new IOException("Couldn't use or create " + smallImgDir.getAbsolutePath());
        }
        File smallFile = new File(smallImgDir, isbn + ".jpg");
        BufferedImage smallImg = resizeImage(bufferedImage, IMG_HEIGHT_SMALL, imgFile);
        ImageIO.write(smallImg, "jpg", smallFile);

        File largeImgDir = new File(imgProcessDir + IMG_LOCATION_LARGE);
        if (!largeImgDir.exists() && !largeImgDir.mkdirs()) {
            throw new IOException("Couldn't use or create " + largeImgDir.getAbsolutePath());
        }
        File largeFile = new File(largeImgDir, isbn + ".jpg");
        BufferedImage largeImg = resizeImage(bufferedImage, IMG_HEIGHT_LARGE, imgFile);
        ImageIO.write(largeImg, "jpg", largeFile);

        ImageData imageData = new ImageData(isbn, imgFile.lastModified());
        imageDataMap.put(isbn, imageData);
    }

    private File getImageFile(String isbn) throws FileNotFoundException {
        File dir = new File(imgUploadLocation);
        if (!dir.exists()) {
            throw new FileNotFoundException("Couldn't find dir " + imgUploadLocation);
        }
        File img = new File(dir, isbn + ".jpg");
        if (!img.exists()) {
            img = new File(dir, isbn + ".png");
        }
        if (!img.exists()) {
            log.error("couldn't find jpg or png file for {}", isbn);
            throw new FileNotFoundException(img.getAbsolutePath());
        }
        return img;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int imgHeight, File inputFile) throws IOException {
        float oHeight = originalImage.getHeight();
        float factor = imgHeight / oHeight;
        int oWidth = originalImage.getWidth();
        int imgWidth = (int) (oWidth * factor);

        File tempFile = File.createTempFile("img", "jpg");
        tempFile.deleteOnExit();
        if (resizeMagick(inputFile.getAbsolutePath(), "100", imgWidth + "x" + imgHeight, tempFile.getAbsolutePath())) {
            BufferedImage image = ImageIO.read(tempFile);
            if (!tempFile.delete()) {
                log.error("Couldn't delete temporary image {}", tempFile.getAbsolutePath());
            }
            return image;
        }

        BufferedImage resizedImage = new BufferedImage(imgWidth, imgHeight, TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, imgWidth, imgHeight, null);
        g.dispose();

        return resizedImage;
    }

    private static boolean resizeMagick(String imagePath, String quality, String size, String output_image) {
        if (!new File(imagePath).exists()) {
            return false;
        }
        String convertPath = null;
        for (String candidate : MAGICK_CANDIDATES) {
            File f = new File(candidate);
            if (f.exists()) {
                convertPath = f.getAbsolutePath();
                break;
            }
        }
        if (convertPath == null) {
            log.warn("Couldn't find ImageMagick in {}", Arrays.asList(MAGICK_CANDIDATES));
            return false;
        }
        ProcessBuilder pb = new ProcessBuilder(
                convertPath,
                imagePath,
                "-quality",
                quality,
                "-resize",
                size,
                output_image);

        pb.redirectErrorStream(true);
        try {
            Process p = pb.start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    log.info(line);
                }
            }
        } catch (Throwable e) {
            return false;
        }

        return true;
    }

}
