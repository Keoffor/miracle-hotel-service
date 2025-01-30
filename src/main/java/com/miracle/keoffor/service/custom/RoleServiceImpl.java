package com.miracle.keoffor.service.custom;

import com.miracle.keoffor.constants.RoleConstant;
import com.miracle.keoffor.exception.RoleAlreadyExistsException;
import com.miracle.keoffor.model.Role;
import com.miracle.keoffor.model.User;
import com.miracle.keoffor.model.dtos.UserDto;
import com.miracle.keoffor.repository.RoleRepository;
import com.miracle.keoffor.repository.UserRepository;
import com.miracle.keoffor.service.RoleService;
import com.miracle.keoffor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role newRole) {
        String roleName = RoleConstant.ROLE.getRoleName()+newRole.getName().toUpperCase();
        Role role = new Role(roleName);

        if(roleRepository.existsByName(role.getName())){
            throw new RoleAlreadyExistsException(newRole.getName()+" role already exists");
        }
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void deleteRole(Long roleId) {
     removeAllUserFromRole(roleId);
     roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public UserDto removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return UserDto.convertToDTO(user.get());
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public UserDto assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new RoleAlreadyExistsException(user.get().getFirstName()+ " is already assigned to the" +
                    " " + role.get().getName()+ " role");
        }
        if(role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return UserDto.convertToDTO(user.get());
    }

    @Override
    public Role removeAllUserFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.ifPresent(Role::removeAllUsersFromRole);
        return roleRepository.save(role.get());
    }
}
