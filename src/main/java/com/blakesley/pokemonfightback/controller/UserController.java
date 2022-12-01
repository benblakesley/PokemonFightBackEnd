package com.blakesley.pokemonfightback.controller;

import com.blakesley.pokemonfightback.model.User;
import com.blakesley.pokemonfightback.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return this.userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public User  getUserByUsername(@PathVariable("username") String username){
        return this.userService.getUserByUsername(username);
    }

    @PostMapping("/create")
    public User createNewUser(@RequestBody User user){

        return this.userService.createNewUser(user);
    }

    @PutMapping("/update")
    public User updateUserScore(@RequestBody User user){
        return this.userService.updateUserScore(user);
    }

    @DeleteMapping("/delete/{username}")
    public User deleteUser(@PathVariable("username") String username){
        return this.userService.deleteUser(username);
    }

    @PostMapping("/checkCredentials")
    public boolean checkCredentials(@RequestBody User user){
        String username = user.getUsername();
        String password = user.getPassword();
        return this.userService.checkCredentials(username,password);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpSession session){
        return this.userService.createSession(user, session);
    }

    @GetMapping("/validateSession")
    public Boolean validateSession(HttpSession session){
        return this.userService.validateSession(session);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session){
        this.userService.logout(session);
    }

}
