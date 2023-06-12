package hexlet.code.app.controller;

import hexlet.code.app.config.JWTHelper;
import hexlet.code.app.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

@AllArgsConstructor
@RestController()
@RequestMapping("${base-url}")
public class LoginController {
    private final UserDetailsServiceImpl userDetailsService;
    private JWTHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> map) {
        UserDetails user = userDetailsService.loadUserByUsername(map.get("email"));
        String password = map.get("password");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UsernameNotFoundException("");
        }
        return jwtHelper.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, user.getUsername()));
    }
}
