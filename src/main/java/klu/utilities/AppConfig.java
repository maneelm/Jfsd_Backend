package klu.utilities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CSVParser csvParser() {
        return new CSVParser();
    }
}
