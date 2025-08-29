package com.divillafajar.app.pos.pos_app_sini.controller.ws;

import com.divillafajar.app.pos.pos_app_sini.model.shared.dto.jwt.JwtLoginRequestDTO;
import com.divillafajar.app.pos.pos_app_sini.model.shared.dto.jwt.JwtLoginResponseDTO;
import com.divillafajar.app.pos.pos_app_sini.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtLoginRequestDTO loginRequest) {
        System.out.println("loginlogin");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    )
            );

            String token = jwtUtil.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(new JwtLoginResponseDTO(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
