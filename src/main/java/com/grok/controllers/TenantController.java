package com.grok.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.grok.entity.Tenant;
import com.grok.services.tenant.TenantService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/tenant")
public class TenantController implements BaseController<Tenant, Integer> {

    @Autowired
    private TenantService tenantService;

    @PostMapping
    public ResponseEntity<Tenant> create(@RequestBody Tenant tenant) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.saveTenant(tenant)); // or status 201
    }

    @GetMapping
    public List<Tenant> index() {
        return tenantService.getAllTenants();
    }

    @GetMapping("{id}")
    public Tenant show(@PathVariable Integer id) {
        return tenantService.findById(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<Tenant> update(@PathVariable Integer id, @RequestBody Tenant tenantDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.updateTenant(id, tenantDetails));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        tenantService.deleteTenantById(id);
    }

}
