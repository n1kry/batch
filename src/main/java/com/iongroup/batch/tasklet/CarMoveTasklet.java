package com.iongroup.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Component
@StepScope
public class CarMoveTasklet implements Tasklet {

    @Value("${file.input.path}")
    private String inputPath;

    @Value("${file.output.path}")
    private String outputPath;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // Логика перемещения файла
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);

        Files.move(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return RepeatStatus.FINISHED;
    }
}
