/**
 * Author: 박기현 (kiryanchi)
 */

package com.gdsc.canigraduate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${springdoc.title}") String title,
            @Value("${springdoc.version}") String version,
            @Value("${springdoc.description}") String description,
            @Value("${springdoc.server_host}") String server_host
    ) {
        Info info = new Info()
                .title(title)
                .version(version)
                .description(description);

        return new OpenAPI()
                .addServersItem(new Server().url("https://" + server_host))
                .components(new Components())
                .info(info);
    }
}
