package com.miracle.keoffor.model.dtos;

import com.miracle.keoffor.model.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<RoleDto> roles;

    public static UserDto convertToDTO(User user){
        List<RoleDto> roleDto = user.getRoles().stream()
                .map(u -> {
                    RoleDto role = new RoleDto();
                    role.setId(u.getId());
                    role.setName(u.getName());
                    return role;
                }).toList();
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(roleDto).build();
    }
}
