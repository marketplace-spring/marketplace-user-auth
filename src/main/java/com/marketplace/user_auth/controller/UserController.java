package com.marketplace.user_auth.controller;

import com.marketplace.user_auth.dto.response.UserResponseDTO;
import com.marketplace.user_auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Secured({"ADMIN_USER"})
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/list")
    @Secured({"ADMIN_USER"})
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(userService.findUserByIds(ids));
    }
}
