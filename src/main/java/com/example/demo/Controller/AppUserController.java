package com.example.demo.Controller;

import com.example.demo.Obj.app_user.AppUserRequest;
import com.example.demo.Obj.app_user.AppUserResponse;
import com.example.demo.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {

    @Autowired
    AppUserService service;

    @PostMapping
    public ResponseEntity<AppUserResponse> createUser(@Valid @RequestBody AppUserRequest request){
        AppUserResponse user = service.createUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") String id) {
        AppUserResponse user = service.getUserResponseById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponse>> getUsers() {
        List<AppUserResponse> users = service.getUsersResponse();

        return ResponseEntity.ok(users);
    }

}
