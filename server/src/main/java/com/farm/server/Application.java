package com.farm.server;
import java.util.logging.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class Application {
	private static Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        logger.info("Initializing Server ");

        SpringApplication application = new SpringApplication(Application.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);        
    }
}

