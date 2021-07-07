package com.example.demo.Service;

import com.example.demo.DAO.AppUserRepository;
import com.example.demo.Exception.ConflictException;
import com.example.demo.Exception.NotFoundException;
import com.example.demo.Obj.app_user.AppUser;
import com.example.demo.Obj.app_user.AppUserRequest;
import com.example.demo.Obj.app_user.AppUserResponse;
import com.example.demo.aop.ActionType;
import com.example.demo.aop.EntityType;
import com.example.demo.aop.SendEmail;
import com.example.demo.converter.AppUserConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

public class AppUserService {


    private AppUserRepository repository;

    private BCryptPasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @SendEmail(entity = EntityType.APP_USER, action = ActionType.CREATE)
    public AppUserResponse createUser(AppUserRequest request) {
        Optional<AppUser> existingUser = repository.findByEmailAddress(request.getEmailAddress());

        if (existingUser.isPresent()) {
            throw new ConflictException("This email address has been used.");
        }

        AppUser user = AppUserConverter.toAppUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = repository.insert(user);

        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUserResponse getUserResponseById(String id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find user."));

        return AppUserConverter.toAppUserResponse(user);
    }

    public AppUser getUserByEmail(String email) {
        return repository.findByEmailAddress(email)
                .orElseThrow(() -> new NotFoundException("Can't find user."));
    }

    public List<AppUserResponse> getUsersResponse(){
        List<AppUser> users = repository.findAll();
        return AppUserConverter.toAppUserResponses(users);
    }

}
