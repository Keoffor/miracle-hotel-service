package com.miracle.keoffor.controller;

import com.miracle.keoffor.exception.RoleAlreadyExistsException;
import com.miracle.keoffor.model.Role;
import com.miracle.keoffor.model.User;
import com.miracle.keoffor.model.dtos.UserDto;
import com.miracle.keoffor.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }
    @PostMapping("/create-role")
    public ResponseEntity<String> createNewRole(@RequestBody Role role){
        try {
            roleService.createRole(role);
            return ResponseEntity.ok("new role created successfully");
        }catch (RoleAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{roleId}/delete")
    public void deleteRole (@PathVariable Long roleId){
      roleService.deleteRole(roleId);
    }

    @PostMapping("/remove-all-users-from-role/{roleId}")
    public ResponseEntity<Role> removeAllUsersFromRole(@PathVariable Long roleId){
        return ResponseEntity.ok(roleService.removeAllUserFromRole(roleId));
    }

    @PostMapping("/remove-user-from-role")
    public ResponseEntity<UserDto> removeUserFromRole(
            @RequestParam Long userId,
            @RequestParam Long roleId){
         return new ResponseEntity<>(roleService.removeUserFromRole(userId,roleId),HttpStatus.CREATED);
    }

    @PostMapping("/assign-user-role")
    public ResponseEntity<UserDto> assignRoleToUser (
            @RequestParam Long userId,
            @RequestParam Long roleId){
        return new ResponseEntity<>(roleService.assignRoleToUser(userId,roleId),HttpStatus.CREATED);
    }

}
