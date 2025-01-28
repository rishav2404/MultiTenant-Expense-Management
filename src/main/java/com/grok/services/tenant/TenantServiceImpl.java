package com.grok.services.tenant;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grok.entity.Tenant;
import com.grok.exceptions.ResourceNotFoundException;
import com.grok.repository.TenantRepository;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    public Tenant saveTenant(Tenant Tenant) {
        return tenantRepository.save(Tenant);
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    public Tenant findById(Integer tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not Found for TenantId: " + tenantId));
    }

    @Override
    public Tenant updateTenant(Integer tenantId, Tenant tenantDetails) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("No record found to update for id: " + tenantId));

        if (Objects.nonNull(tenantDetails.getName())
                && !"".equalsIgnoreCase(
                        tenantDetails.getName())) {
            tenant.setName(tenantDetails.getName());
        }
        return tenantRepository.save(tenant);
    }

    @Override
    public void deleteTenantById(Integer tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("No record found to delete for id: " + tenantId));
        tenantRepository.delete(tenant);
    }

}
