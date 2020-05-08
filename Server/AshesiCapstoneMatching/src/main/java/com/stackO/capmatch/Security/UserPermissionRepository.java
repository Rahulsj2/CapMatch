package com.stackO.capmatch.Security;

import org.springframework.data.repository.CrudRepository;

public interface UserPermissionRepository extends CrudRepository<UserPermission, Integer> {
	
	public UserPermission findByName(String name);

}
