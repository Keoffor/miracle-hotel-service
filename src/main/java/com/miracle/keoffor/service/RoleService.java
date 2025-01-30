package com.miracle.keoffor.service;

import com.miracle.keoffor.model.Role;
import com.miracle.keoffor.model.User;
import com.miracle.keoffor.model.dtos.UserDto;

import java.util.List;

public interface RoleService {

    List<Role> getRoles ();

    Role createRole(Role newRole);

    void deleteRole(Long id);

    Role findByName(String name);

    UserDto removeUserFromRole(Long userId, Long roleId );

    UserDto assignRoleToUser(Long userId, Long roleId);

    Role removeAllUserFromRole(Long roleId);
}
