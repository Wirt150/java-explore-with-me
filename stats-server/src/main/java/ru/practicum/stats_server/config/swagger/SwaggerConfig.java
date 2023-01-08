package ru.practicum.stats_server.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI dataOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Дипломный проект YandexPracticum (explore-with-me-stats-server)")
                                .version("0.0.1")
                                .description("Севрер статистики приложения explore-with-me.")
                                .contact(
                                        new Contact()
                                                .email("Wirt150@gmail.ru")
                                                .url("https://github.com/Wirt150")
                                                .name("Виктор Аксенов")
                                )
                );
    }
}