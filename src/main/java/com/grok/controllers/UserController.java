package com.grok.controllers;

import com.grok.entity.Tenant;
import com.grok.entity.User;
import com.grok.services.tenant.TenantService;
import com.grok.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TenantService tenantService;

    @GetMapping("/get-users")
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e);
        }
    }

    @GetMapping("/id/{userId}/get-user")
    public ResponseEntity<?> getUser(@PathVariable Integer userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            Tenant tenant = user.getTenant();
            if (tenant != null && tenant.getId() != null) {
                tenant = tenantService.findById(tenant.getId());
                if (tenant == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tenant with the provided ID not found!!.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tenant ID is required.");
            }

            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/id/{userId}/update-user")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId, @RequestBody User userDetails) {
        try {
            User user = userService.updateUserById(userId, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/id/{userId}/delete")
    public ResponseEntity<?> delete(@PathVariable Integer userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}