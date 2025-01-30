package com.miracle.keoffor.service.custom;

import com.miracle.keoffor.constants.RoleConstant;
import com.miracle.keoffor.exception.RoleNotFoundException;
import com.miracle.keoffor.exception.UserAleadyExistException;
import com.miracle.keoffor.model.Role;
import com.miracle.keoffor.model.User;
import com.miracle.keoffor.model.dtos.RoleDto;
import com.miracle.keoffor.model.dtos.UserDto;
import com.miracle.keoffor.repository.RoleRepository;
import com.miracle.keoffor.repository.UserRepository;
import com.miracle.keoffor.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserDto::convertToDTO
        ).toList();
    }

    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw  new UserAleadyExistException(user.getEmail()+ " already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(
                RoleConstant.ROLE.getRoleName()+RoleConstant.USER.getRoleName())
                .orElseThrow(() -> new RoleNotFoundException("This role does not exist"));

        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        UserDto theUser = getUser(email);
        if(theUser != null) {
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public UserDto getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return UserDto.convertToDTO(user);

        }


}
