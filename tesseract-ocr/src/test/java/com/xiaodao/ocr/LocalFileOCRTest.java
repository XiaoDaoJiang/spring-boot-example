package com.xiaodao.ocr;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;

import java.io.File;

public class LocalFileOCRTest {

    @Test
    public void testOCR() {
        // 1.class path:linux-x86-64/ √
        // 2.file:/usr/lib64/ √
        // x test failed 3. or java.library.path =
//        System.setProperty("java.library.path", "/home/linux-x86-64/");
//        System.out.println(System.getProperty("java.library.path"));
//        System.loadLibrary("tesseract");
        File imageFile = new File("/home/spring-boot-example/tesseract-ocr/src/test/resouces/images/phototest.tif");
        // ITesseract instance = new Tesseract();  // JNA Interface Mapping
        ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        // /usr/share/tesseract/4/tessdata
        instance.setDatapath("/home/spring-boot-example/tesseract-ocr/src/test/resouces/tessdata"); // path to tessdata directory

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
