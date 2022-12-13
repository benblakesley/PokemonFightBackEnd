package com.blakesley.pokemonfightback.service;

import com.blakesley.pokemonfightback.model.User;
import com.blakesley.pokemonfightback.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User createNewUser(User user) {
        return this.userRepo.save(user);
    }

    public User getUserByUsername(String username) {
        Optional<User> optionalOfUser = this.userRepo.findByUsername(username);
        if(optionalOfUser.isEmpty()){
            return null;
        }
        else{
            return optionalOfUser.get();
        }

    }

    public User updateUserScore(String username, Integer score) {
       Optional<User> optionalOfUser = this.userRepo.findByUsername(username);
       if(optionalOfUser.isEmpty()){
           return null;
       }
       else{
           User userToUpdate = optionalOfUser.get();
           userToUpdate.setScore(score);
           return this.userRepo.save(userToUpdate);
       }
    }

    public List<User> getAllUsers() {
        return (List<User>) this.userRepo.findAll();
    }

    public List<User> getAllWithoutPasswords(){
        List<User> allUsers = this.userRepo.findAll();

        allUsers.forEach(user -> {
            user.setPassword("fakePassword");
        });

        return allUsers;

    }

    public User deleteUser(String username) {
        Optional<User> userToDeleteOptional = this.userRepo.findByUsername(username);
        if(userToDeleteOptional.isEmpty()){
            return null;
        }
        User userToDelete = userToDeleteOptional.get();
        this.userRepo.delete(userToDelete);
        return userToDelete;
    }

    public Boolean checkCredentials(String username, String password){
        Optional<User> userOptional = this.userRepo.findByUsername((username));
        if(userOptional.isEmpty()){
            return false;                //check if user exists
        }
        User user = userOptional.get(); //this is not null since user exists
        if(user.getPassword().equals(password)){
            return true;          // check is entered password is the same as user's password
        }
        else{
            return false;        // return false if passwords dont match
        }
    }

    public String createSession(User user, HttpSession session){
        String username = user.getUsername();
        User userInDb = this.userRepo.findByUsername(username).get();
        session.setAttribute("user", userInDb);
        return username;
    }

    public Boolean validateSession(HttpSession session){
        Boolean activeSession;

        if(session.getAttribute("user")==null){
            activeSession=false;
        }
        else{
            activeSession=true;
        }
        return activeSession;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public String getCurrentUser(HttpSession session) {
        if(session.getAttribute("user")==null){
            return "";
        }
        else{
            User user = (User) session.getAttribute("user");
            String username = user.getUsername();
            return username;
        }
    }

    public Integer getScoreByUsername(String username) {
        Optional<User> userOptional = this.userRepo.findByUsername(username);
        if(userOptional.isEmpty()){
            return 0;
        }
        User user = userOptional.get();
        Integer userScore = user.getScore();
        if(userScore==null){
            return 0;
        }
        return userScore;

    }


}
