package hexlet.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Welcome")
public class WelcomeController {

    @Operation(summary = "Welcome", description = "Welcome to Spring.")
    @ApiResponses(@ApiResponse(responseCode = "201", content =
    @Content(mediaType = "text/plain")
    ))
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring";
    }
}
