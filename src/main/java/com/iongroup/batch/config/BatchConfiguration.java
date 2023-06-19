package com.iongroup.batch.config;

import com.iongroup.batch.dao.CarDAO;
import com.iongroup.batch.model.Car;
import com.iongroup.batch.model.dto.CarDTO;
import com.iongroup.batch.processor.CarValidationProcessor;
import com.iongroup.batch.tasklet.CarMoveTasklet;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfiguration {

    private CarDAO carDAO;

    @Bean
    public FlatFileItemReader<CarDTO> carReader() {
        FlatFileItemReader<CarDTO> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("cars.csv"));
        reader.setName("csvReader");
        reader.setLinesToSkip(1); // Пропустить заголовок CSV-файла

        reader.setLineMapper(lineMapper());

        return reader;
    }

    private LineMapper<CarDTO> lineMapper() {
        DefaultLineMapper<CarDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("model", "maker", "carNumber", "madeYear", "startedRentOn", "finishedRentOn");

        BeanWrapperFieldSetMapper<CarDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CarDTO.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public CarValidationProcessor carProcessor() {
        return new CarValidationProcessor();
    }

    @Bean
    public RepositoryItemWriter<Car> carWriter() {

        RepositoryItemWriter<Car> writer = new RepositoryItemWriter<>();
        writer.setRepository(carDAO);
        writer.setMethodName("save");

        return writer;
    }

//    @Bean
//    public Step carImportStep(ItemReader<CarDTO> reader, ItemProcessor<CarDTO, Car> processor, ItemWriter<Car> writer, CarMoveTasklet tasklet) {
//        return stepBuilderFactory.get("carImportStep")
//                .<CarDTO, Car>chunk(10)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }

    @Bean
    public Step step1(JobRepository repository,
                      PlatformTransactionManager transactionManager,
                      FlatFileItemReader<CarDTO> carReader,
                      CarValidationProcessor carProcessor,
                      RepositoryItemWriter<Car> carWriter) {
        return new StepBuilder("csv-step", repository)
                .<CarDTO, Car>chunk(10, transactionManager)
                .reader(carReader)
                .processor(carProcessor)
                .writer(carWriter)
                .build();
    }

    @Bean
    public Step carMoveStep(JobRepository repository, PlatformTransactionManager transactionManager, CarMoveTasklet tasklet) {
        return new StepBuilder("carMoveStep", repository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Job runJob(JobRepository repository, Step step1, Step carMoveStep) {
        return new JobBuilder("importCars", repository)
                .flow(step1)
                .next(carMoveStep)
                .end().build();
    }
//
//    @Bean
//    public Job carImportJob(JobExecutionListener listener, Step carImportStep, Step carMoveStep) {
//        return new JobBuilder("carImportJob")
//                .incrementer(new RunIdIncrementer())
//                .listener(listener)
//                .start(carImportStep)
//                .next(carMoveStep)
//                .build();
//    }
}