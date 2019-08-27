package com.banking_app.service;

import com.banking_app.model.Role;


public interface RoleService {
	Role save(Role role);
	Role getUserRole();
	Role getAdminRole();
   
}
