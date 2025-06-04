package org.example.apitest.controller;

import jakarta.validation.Valid;
import org.example.apitest.dto.ApiResponse;
import org.example.apitest.dto.RegisterRequest;
import org.example.apitest.model.User;
import org.example.apitest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterRequest request,
                                                    BindingResult bindingResult) {
        try {
            // Check validation errors
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                bindingResult.getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Dữ liệu không hợp lệ", errors));
            }

            // Register user
            User user = userService.registerUser(request);

            // Return success response (without password)
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Đăng ký thành công", userData));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Lỗi hệ thống"));
        }
    }
}