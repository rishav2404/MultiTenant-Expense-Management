package com.grok.controllers;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.grok.entity.Tenant;
import com.grok.entity.User;
import com.grok.services.tenant.TenantService;
import com.grok.services.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
   @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TenantService tenantService;

    @Test
    public void testGetUsers_Success() throws Exception {
        
        Tenant mockTenant = new Tenant(1, "Sample Tenant");

        List<User> mockUsers = List.of(
            new User(1, mockTenant, "john@example.com", "password123", "USER"),
            new User(2, mockTenant, "jane@example.com", "password456", "ADMIN")
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/user/get-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[0].tenant.name").value("Sample Tenant"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"))
                .andExpect(jsonPath("$[1].tenant.name").value("Sample Tenant"));
    }

    @Test
    public void testGetUsers_Exception() throws Exception {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/user/get-users"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message").value("Database error"));
    }

    @Test
    public void testGetUser_Success() throws Exception {
        Integer userId = 1;
        Tenant mockTenant = new Tenant(1, "Sample Tenant");
        User mockUser = new User(userId, mockTenant, "john@example.com", "password123", "USER");

        when(userService.getUserById(userId)).thenReturn(mockUser);

        mockMvc.perform(get("/user/id/{userId}/get-user", userId))  
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$.email").value("john@example.com"))  
                .andExpect(jsonPath("$.role").value("USER")) 
                .andExpect(jsonPath("$.tenant.name").value("Sample Tenant")); 
    }

    @Test
    public void testGetUser_NotFound() throws Exception {
        Integer userId = 99;

        when(userService.getUserById(userId)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/user/id/{userId}/get-user", userId)) 
                .andExpect(status().isNotFound()) 
                .andExpect(jsonPath("$.message").value("User not found")); 
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        Tenant mockTenant = new Tenant(1, "Mock Tenant");
        User mockUser = new User(1, mockTenant, "john@example.com", "password123", "USER");

        when(tenantService.findById(mockTenant.getId())).thenReturn(mockTenant);

        when(userService.createUser(any(User.class))).thenReturn(mockUser);

        mockMvc.perform(post("/user/create-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{ \"email\": \"john@example.com\", \"password\": \"password123\", \"role\": \"USER\", \"tenant\": { \"id\": 1 } }"))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.email").value("john@example.com"))  
                .andExpect(jsonPath("$.role").value("USER"))  
                .andExpect(jsonPath("$.tenant.id").value(1));
    }

    @Test
    public void testCreateUser_TenantIdMissing() throws Exception {
        String userJson = "{ \"email\": \"john@example.com\", \"password\": \"password123\", \"role\": \"USER\" }";

        mockMvc.perform(post("/user/create-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().isBadRequest()) 
                .andExpect(jsonPath("$").value("Tenant ID is required."));
    }

    @Test
    public void testCreateUser_TenantNotFound() throws Exception {
        Tenant mockTenant = new Tenant(99, "Non-existing Tenant");
        User mockUser = new User(1, mockTenant, "john@example.com", "password123", "USER");

        when(tenantService.findById(mockTenant.getId())).thenReturn(null);

        mockMvc.perform(post("/user/create-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{ \"email\": \"john@example.com\", \"password\": \"password123\", \"role\": \"USER\", \"tenant\": { \"id\": 99 } }"))
                .andExpect(status().isBadRequest()) 
                .andExpect(jsonPath("$").value("Tenant with the provided ID not found!!."));
    }

    @Test
    public void testCreateUser_InternalServerError() throws Exception {
        Tenant mockTenant = new Tenant(1, "Sample Tenant");
        User mockUser = new User(1, mockTenant, "john@example.com", "password123", "USER");

        when(tenantService.findById(mockTenant.getId())).thenReturn(mockTenant);

        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(post("/user/create-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{ \"email\": \"john@example.com\", \"password\": \"password123\", \"role\": \"USER\", \"tenant\": { \"id\": 1 } }"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("Internal Server Error"));
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        Tenant mockTenant = new Tenant(1, "Updated Tenant");
        User updatedUser = new User(1, mockTenant, "updated@example.com", "newpassword123", "ADMIN");
        User userDetails = new User(null, mockTenant, "updated@example.com", "newpassword123", "ADMIN");
    
        when(userService.updateUserById(eq(1), any(User.class))).thenReturn(updatedUser);
    
        mockMvc.perform(put("/user/id/1/update-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{ \"email\": \"updated@example.com\", \"password\": \"newpassword123\", \"role\": \"ADMIN\", \"tenant\": { \"id\": 1 } }"))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.email").value("updated@example.com")) 
                .andExpect(jsonPath("$.role").value("ADMIN")) 
                .andExpect(jsonPath("$.tenant.id").value(1)); 
    }
    
    @Test
    public void testUpdateUser_Failure() throws Exception {
        when(userService.updateUserById(eq(99), any(User.class)))
                .thenThrow(new RuntimeException("User not found"));
    
        mockMvc.perform(put("/user/id/99/update-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{ \"email\": \"updated@example.com\", \"password\": \"newpassword123\", \"role\": \"ADMIN\", \"tenant\": { \"id\": 1 } }"))
                .andExpect(status().isBadRequest()) 
                .andExpect(content().string("User not found")); 
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUserById(1);

        mockMvc.perform(delete("/user/id/1/delete"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    public void testDeleteUser_Failure() throws Exception {
        doThrow(new RuntimeException("User not found")).when(userService).deleteUserById(999);

        mockMvc.perform(delete("/user/id/999/delete"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }
}