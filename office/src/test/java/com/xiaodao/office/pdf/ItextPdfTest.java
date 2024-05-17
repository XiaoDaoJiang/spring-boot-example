package com.xiaodao.office.pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author jianghaitao
 * @Classname ItextPdfTest
 * @Version 1.0.0
 * @Date 2024-05-17 09:52
 * @Created by jianghaitao
 */
@ExtendWith(MockitoExtension.class)
public class ItextPdfTest {


    String fontPath = "E:\\usr\\share\\fonts\\chinese.stsong.ttf"; // 替换成你的中文字体路径


    // HTML 内容
    String htmlContent = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\"/>\n" +
            "</head>\n" +
            "<body>\n" +
            "<table style=\"width: 100%;table-layout: fixed;text-align: center;border-collapse: collapse;margin-top: 10px\">\n" +
            "    <thead>\n" +
            "    <tr>\n" +
            "        <th style=\"width: 50%; border: 1px solid; padding: 8px;white-space: nowrap; text-align: center;\">评估项目\n" +
            "        </th>\n" +
            "        <th style=\"width: 15%; border: 1px solid; padding: 8px;white-space: nowrap; text-align: center;\">评估顺序\n" +
            "        </th>\n" +
            "        <th style=\"width: 10%; border: 1px solid; padding: 8px;white-space: nowrap; text-align: center;\">状态\n" +
            "        </th>\n" +
            "        <th style=\"width: 10%; border: 1px solid; padding: 8px; text-align: center;\">总分</th>\n" +
            "        <th style=\"width: 10%; border: 1px solid; padding: 8px; text-align: center;\">得分</th>\n" +
            "        <th style=\"width: 15%; border: 1px solid; padding: 8px; text-align: center;\">改善率</th>\n" +
            "    </tr>\n" +
            "    </thead>\n" +
            "    <tbody>\n" +
            "    <tr>\n" +
            "        <td style=\"/* border: 1px solid; */padding: 8px;text-align: center;border-style: solid none none solid;border-width: 1px;\">\n" +
            "            国际运动障碍学会帕金森病综合评价量表（MDS-UPDRS-III）\n" +
            "        </td>\n" +
            "        <td style=\"/* border: 1px solid; */padding: 8px;text-align: center;border-style: solid none none solid;border-width: 1px;\">基线</td>\n" +
            "        <td style=\"/* border: 1px solid; */padding: 8px;text-align: center;border-style: solid none none solid;border-width: 1px;\">开期;未手术</td>\n" +
            "        <td style=\"/* border: 1px solid; */padding: 8px;text-align: center;border-style: solid none none solid;border-width: 1px;\">132.0</td>\n" +
            "        <td style=\"/* border: 1px solid; */padding: 8px;border-style: solid none none solid;border-width: 1px;text-align: center;\">80.0</td>\n" +
            "        <td style=\"/* border: 1px solid; */padding: 8px;text-align: center;border-style: solid solid none solid;border-width: 1px;\">--</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">\n" +
            "            国际运动障碍学会帕金森病综合评价量表（MDS-UPDRS-III）\n" +
            "        </td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">第一次</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">开期;未手术</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">132.0</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">11.0</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">86.2%</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">\n" +
            "            国际运动障碍学会帕金森病综合评价量表（MDS-UPDRS-III）\n" +
            "        </td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">第二次</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">开期;未手术</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">132.0</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">0.0</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">--</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">10分画钟试验</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">--</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">--</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">10</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">0.0</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">--</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">副作用监测量表</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">--</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">--</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">272.0</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">7.0</td>\n" +
            "        <td style=\"border: 1px solid; padding: 8px; text-align: center;\">--</td>\n" +
            "    </tr>\n" +
            "    </tbody>\n" +
            "</table>\n" +
            "</body>\n" +
            "</html>\n";

    // 输出的 PDF 文件路径
    String outputPath = "/opt/output.pdf";

    @Test
    public void createPdf() throws Exception {
        try {
            // 将 HTML 转换成 PDF
            HtmlConverter.convertToPdf(htmlContent,
                    Files.newOutputStream(Paths.get(outputPath)));
            System.out.println("PDF 已成功生成：" + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void html2Pdf() throws IOException {
        // 配置 FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(ItextPdfTest.class, "/");
        cfg.setDefaultEncoding("UTF-8");


        // 数据模型
        Map<String, Object> model = new HashMap<>();
        model.put("user", "John Doe");

        // 初始化字体供应器并添加中文字体
        FontProvider fontProvider = new FontProvider();
        fontProvider.addFont(FontProgramFactory.createFont(fontPath));

        // 设置转换属性并关联字体供应器
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setFontProvider(fontProvider);

        // 生成 HTML
        try (StringWriter stringWriter = new StringWriter()) {
            Template template = cfg.getTemplate("template/reportTable.ftl");
            template.process(model, stringWriter);
            String htmlContent = stringWriter.toString();

            // 将 FreeMarker 生成的 HTML 转换成 PDF
            HtmlConverter.convertToPdf(htmlContent, Files.newOutputStream(Paths.get(outputPath)), converterProperties);
            System.out.println("PDF 文件生成成功：" + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
