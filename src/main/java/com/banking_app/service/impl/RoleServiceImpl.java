package com.banking_app.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking_app.dao.RoleRepository;
import com.banking_app.model.Role;
import com.banking_app.service.RoleService;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService{
    
	@Autowired
	private RoleRepository roleRepo;
	
	@Override
	public Role save(Role role) {
		 return roleRepo.save(role);
		
	}

	@Override
	public Role getUserRole() {
		// TODO Auto-generated method stub
		return roleRepo.findOneByRoleId((long) 2);
	}

	@Override
	public Role getAdminRole() {
		// TODO Auto-generated method stub
		return roleRepo.findOneByRoleId((long) 1);
	}


	

}
