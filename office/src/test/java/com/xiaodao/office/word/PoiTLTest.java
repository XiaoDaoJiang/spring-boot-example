package com.xiaodao.office.word;

import com.deepoove.poi.XWPFTemplate;
import com.xiaodao.office.pdf.ItextPdfTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author jianghaitao
 * @Classname PoiTLTest
 * @Version 1.0.0
 * @Date 2024-05-17 11:06
 * @Created by jianghaitao
 */
@ExtendWith(MockitoExtension.class)
public class PoiTLTest {


    @Test
    public void test() throws IOException {
        File templateFile = new File("E:\\IdeaProjects\\learn\\spring-boot-example\\office\\src\\test\\resources\\template\\template.docx");
        XWPFTemplate template = XWPFTemplate.compile(templateFile).render(
                new HashMap<String, Object>(){{
                    final ItextPdfTest.ScalePointModel scalePointModel = new ItextPdfTest.ScalePointModel();
                    scalePointModel.setScaleGrade("132");
                    scalePointModel.setScalePoint("1");
                    scalePointModel.setState("开机;未手术");
                    scalePointModel.setGaugeName("国际运动障碍学会帕金森病综合评价量表（MDS-UPDRS-III）");
                    scalePointModel.setImproveRate("50%");
                    put("scalePointModel", scalePointModel);
                }});
        template.writeAndClose(Files.newOutputStream(Paths.get("output.docx")));
    }

}
