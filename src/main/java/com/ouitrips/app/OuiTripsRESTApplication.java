package com.ouitrips.app;

import com.ouitrips.app.config.RsakeysConfig;
import com.ouitrips.app.utils.encryptutil.RSAKeysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({RsakeysConfig.class, RSAKeysConfig.class})
@EnableScheduling
public class OuiTripsRESTApplication {
	private static final Logger logger = LoggerFactory.getLogger(OuiTripsRESTApplication.class);


    public static void main(String[] args) {
		for (String arg : args) {
			if (arg.startsWith("--server.port=")) {
				String port = arg.substring(arg.indexOf("=") + 1);
				System.setProperty("server.port", port);
				break;
			}
		}
		SpringApplication.run(OuiTripsRESTApplication.class, args);
		logger.info("############# Application Started #############");
	}

	@Bean
	CommandLineRunner commandLineRunner(
	) {
		return args -> {

		};
		}
	}
