package com.grok.services.tenant;

import java.util.List;
import com.grok.entity.Tenant;

public interface TenantService {
    Tenant saveTenant(Tenant Tenant);

    List<Tenant> getAllTenants();

    Tenant findById(Integer id);

    Tenant updateTenant(Integer id, Tenant tenantDetail);

    void deleteTenantById(Integer tenantId);
}
