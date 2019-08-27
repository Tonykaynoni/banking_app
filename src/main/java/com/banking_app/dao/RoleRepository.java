package com.banking_app.dao;


import org.springframework.data.repository.CrudRepository;

import com.banking_app.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
  Role findOneByRoleId(Long RoleId);


}
