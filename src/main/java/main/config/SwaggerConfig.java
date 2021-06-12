package main.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("My Blog Core API")
                                .version("1.0.0")
                                .description("My diploma project")
                                .contact(new Contact()
                                        .name("Pavel Vladysik")
                                        .email("dude1410@mail.ru"))
                );
    }
}
