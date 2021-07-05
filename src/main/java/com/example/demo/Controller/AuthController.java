package com.example.demo.Controller;

import com.example.demo.Service.JWTService;
import com.example.demo.objRequest.AuthRequest;
import com.sun.jdi.InvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private JWTService jwtService;

    @PostMapping
    public ResponseEntity<Map<String, String>> generateToken(@Valid @RequestBody AuthRequest request) {
        System.out.println("Username: " + request.getUsername());
        System.out.println("Password: " + request.getPassword());
        try {
            String token = jwtService.generateToken(request);
            Map<String, String> response = Collections.singletonMap("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Cause of exception: " + e.toString());
        }

//        return ResponseEntity.ok(response);
        return null;


    }

    @PostMapping("/parse")
    public ResponseEntity<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        Map<String, Object> response = jwtService.parseToken(token);

        return ResponseEntity.ok(response);
    }


}
