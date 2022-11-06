package com.xiaodao.ocr.Controller;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("ocr")
public class ImageOCRController {

    @RequestMapping("hello")
    public String hello(MultipartFile file) {
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        // /usr/share/tesseract/4/tessdata
        instance.setDatapath("E:\\learning\\project\\spring-boot-example\\tesseract-ocr\\src\\main\\resouces\\tessdata"); // path to tessdata directory
        // instance.setLanguage("chi_sim_vert");
        // instance.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_TESSERACT_ONLY);
        instance.setTessVariable("tessedit_char_whitelist", "0123456789");


        try {
            final BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            String result = instance.doOCR(bufferedImage);
            System.out.println(result);
            return result;
        } catch (TesseractException | IOException e) {
            System.err.println(e.getMessage());
            return e.getMessage();
        }
    }
}
