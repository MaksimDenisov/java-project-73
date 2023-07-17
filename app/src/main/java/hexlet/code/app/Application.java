package hexlet.code.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Task manager",
        version = "0.0",
        description = "5 hexlet project"
))
@SecurityScheme(name = "javainuseapi", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
           .profiles("dev") // TODO Add APP_ENV_PROFILE
           .run(args);
    }
}
