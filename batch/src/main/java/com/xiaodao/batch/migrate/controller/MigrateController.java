package com.xiaodao.batch.migrate.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.xiaodao.batch.migrate.support.ValidationRetainingItemProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;


/**
 * @author xiaodaojiang
 * @Classname MigrateController
 * @Version 1.0.0
 * @Date 2024-08-21 01:23
 * @Created by xiaodaojiang
 */
@Slf4j
@Validated
@RequestMapping("migration")
@RestController
public class MigrateController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job demoJob;

    @PostMapping("validate")
    public String migrate(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return "File is empty";
        }

        // Define the upload directory
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
        File uploadDirFile = new File(uploadDir);

        // Create the directory if it does not exist
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // Save the file to the upload directory
        File dest = new File(uploadDir + File.separator + file.getOriginalFilename());
        file.transferTo(dest);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("file", "file:" + dest.getAbsolutePath())
                .addString("outputFile", "file:" + uploadDir + File.separator + "output-"
                        + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT)
                        + file.getOriginalFilename())
                .addDate("date", new Date())
                .toJobParameters();

        final JobExecution run = jobLauncher.run(demoJob, jobParameters);
        log.info("JobExecution: {}", run);

        final ValidationRetainingItemProcessor.ValidationContext validationContext
                = (ValidationRetainingItemProcessor.ValidationContext) run.getStepExecutions().stream().findFirst().get().getExecutionContext().get(ValidationRetainingItemProcessor.VALIDATION_CONTEXT_KEY);
        log.info("validationContext {}", validationContext);

        return "success";
    }
}
