package com.pms.pmsmodule.Helpers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Patient Management API",
                version = "v1",
                description = "API for managing patient records",
                contact = @Contact(name = "Support", email = "support@example.com")
        )
)
public class OpenAPIConfig {}
