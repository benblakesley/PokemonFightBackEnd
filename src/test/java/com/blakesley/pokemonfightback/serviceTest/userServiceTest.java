package com.blakesley.pokemonfightback.serviceTest;


import com.blakesley.pokemonfightback.model.User;
import com.blakesley.pokemonfightback.repository.UserRepo;
import com.blakesley.pokemonfightback.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class userServiceTest {

    // class under test
    private UserService userService;

    //dependency
    private UserRepo userRepo;
    private HttpSession session;

    @BeforeEach
    public void setup(){
        //mock the user repo
        userRepo = mock(UserRepo.class);
        //mock the session
        session = mock(HttpSession.class);

        // instantiate the user service with dependency on the the mocked repo
        userService = new UserService(userRepo);
    }

    @Test
    public void getUserByUsernameTest() {
        // Firstly, test the case where the user exists in the database
        User sampleUser = new User();       // sample user

        //Tell repo what to return when it is asked for user1
        when(userRepo.findByUsername("user1")).thenReturn(Optional.of(sampleUser));

        //the user that is obtained when the method under test is called
        User obtainedUser = userService.getUserByUsername("user1");

        //we expect the obtained user to be the same as the sample user
        assertEquals(sampleUser, obtainedUser);

        //next, handle the case where the user does not exist in database
        //tell repo to return empty optional when searching for user2
        when(userRepo.findByUsername("user2")).thenReturn(Optional.empty());

        //the user returned when the service searches for user2
        User obtainedUserTwo = userService.getUserByUsername("user2");

        //we expect return to be null
        assertEquals(null, obtainedUserTwo);
    }

    @Test
    public void getAllUsersTest(){
        List<User> sampleUsers = new ArrayList<>();
        for (int i=0; i<20; i++){
            sampleUsers.add(new User());      // sample data
        }
        when(userRepo.findAll()).thenReturn(sampleUsers);

        List<User> obtainedUsers = userService.getAllUsers();

        assertEquals(sampleUsers, obtainedUsers);
    }

    @Test
    public void checkCredentialsTest(){
        //First the case where the username does not exist
        when(userRepo.findByUsername("user")).thenReturn(Optional.empty());

        assertEquals(false, userService.checkCredentials("user", "anything"));

        //next check the case where the username exists but password is incorrect

        User sampleUserOne = new User();

        sampleUserOne.setPassword("pass");

        when(userRepo.findByUsername("user1")).thenReturn(Optional.of(sampleUserOne));

        assertEquals(false, userService.checkCredentials("user1", "notPass"));

        //next check the case when credentials are correct

        assertEquals(true, userService.checkCredentials("user1", "pass"));

    }

    @Test
    public void getScoreByUsernameTest(){
        //first the case where the user does not exist
        when(userRepo.findByUsername("user")).thenReturn(Optional.empty());

        Integer scoreOne = userService.getScoreByUsername("user");

        assertEquals(0, scoreOne);

        // next the case where the user exists but the score is null (the case where the user has yet to play a game)

        User userOne = new User();

        when(userRepo.findByUsername("userOne")).thenReturn(Optional.of(userOne));

        Integer scoreTwo = userService.getScoreByUsername("userOne");

        assertEquals(0, scoreTwo);

        //next the case where the user exists and has a non null score

        User userTwo = new User();
        userTwo.setScore(10);

        when(userRepo.findByUsername("userTwo")).thenReturn(Optional.of(userTwo));

        Integer scoreThree = userService.getScoreByUsername("userTwo");

        assertEquals(10, scoreThree);

    }

    @Test
    public void getCurrentSessionTest(){

        //first the case where the session has no user
        assertEquals("", userService.getCurrentUser(session));

        //now add a user to the session
        User sampleUser = new User();
        sampleUser.setUsername("userOne");

        when(session.getAttribute("user")).thenReturn(sampleUser);


        assertEquals("userOne", userService.getCurrentUser(session) );
    }

    @Test
    public void validateSessionTest(){
        //first the case of no active session
        when(session.getAttribute("user")).thenReturn(null);

        assertEquals(false, userService.validateSession(session));

        //next the case of an active session
        User sampleUser = new User();   //sample data
        when(session.getAttribute("user")).thenReturn(sampleUser);

        assertEquals(true, userService.validateSession(session));
    }

    @Test
    public void deleteUserTest(){
        //first the case where the user does not exist

        when(userRepo.findByUsername("userOne")).thenReturn(Optional.empty());

        assertEquals(null, userService.deleteUser("userOne"));

        //next the case where the user exists
        User sampleUser = new User();

        when(userRepo.findByUsername("userTwo")).thenReturn(Optional.of(sampleUser));

        assertEquals(sampleUser, userService.deleteUser("userTwo"));

    }

    @Test
    public void createSessionTest(){
        User sampleUser = new User();
        sampleUser.setUsername("user");
        when(userRepo.findByUsername("user")).thenReturn(Optional.of(sampleUser));
        assertEquals("user", userService.createSession(sampleUser, session));
    }
}
