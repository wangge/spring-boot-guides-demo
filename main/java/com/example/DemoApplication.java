package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.messagingredis.Receiver;
import com.example.uploadingfiles.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@SpringBootApplication
@ComponentScan("com.example.*")
@RestController
@ConfigurationPropertiesScan("com.example.*")
@EnableAsync
public class DemoApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);
    public static void main(String[] args) throws InterruptedException{

        ApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        //send message with redis
        StringRedisTemplate template =ctx.getBean(StringRedisTemplate.class);
        Receiver receiver = ctx.getBean(Receiver.class);

        while(receiver.getCount()== 0){
            LOGGER.info("Sending message...");
            template.convertAndSend("chat", "hello from redis");
            Thread.sleep(500L);
        }
        //System.exit(0);
    }

    @GetMapping("hello")
    public String Hello(@RequestParam(value = "name", defaultValue = "world") String name){
        return String.format("Hello %s!", name);
    }


    @Bean
    CommandLineRunner init(StorageService storageService){
        return (args -> {
           storageService.deleteAll();
           storageService.init();
        });
    }

    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("GithubLoopUp-");
        executor.initialize();
        return executor;
    }
}
