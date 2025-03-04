package com.miracle.keoffor.controller;

import com.miracle.keoffor.model.dtos.UserDto;
import com.miracle.keoffor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

     private final UserService userService;


     @GetMapping("all-users")
     @PreAuthorize("hasRole('ROLE_ADMIN')")
     public ResponseEntity<List<UserDto>> getAllUsers(){
         return new ResponseEntity<List<UserDto>>(userService.getUsers(), HttpStatus.OK);
     }

     @GetMapping("/profile/{email}")
     @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
     public ResponseEntity<?> getUserByEmail(@PathVariable String email){
         try{
             UserDto userDto = userService.getUser(email);
             return ResponseEntity.ok(userDto);
         }catch (UsernameNotFoundException e){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
         }catch (Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
         }
     }

     @DeleteMapping("/delete/{userId}")
     @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #email== principal.username)")
     public ResponseEntity<String> deleteUserByEmail(@PathVariable("userId") String email){
         try{
             userService.deleteUser(email);
             return ResponseEntity.ok("User with email Id - "+email+" successfully deleted");
         }catch (UsernameNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
         }catch (Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
         }
     }

}
