package com.blakesley.pokemonfightback.serviceTest;


import com.blakesley.pokemonfightback.model.User;
import com.blakesley.pokemonfightback.repository.UserRepo;
import com.blakesley.pokemonfightback.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

    @BeforeEach
    public void setup(){
        //mock the user repo
        userRepo = mock(UserRepo.class);

        // instantiate the user service with dependency on the the mocked repo
        userService = new UserService(userRepo);
    }

    @Test
    public void getUserByUsernameTest(){
        // Firstly, test the case where the user exists in the database
        User sampleUser = new User();       // sample user

        //Tell repo what to return when it is asked for user1
        when(userRepo.findByUsername("user1")).thenReturn(Optional.of(sampleUser));

        //the user that is obtained when the method under test is called
        User obtainedUser = userService.getUserByUsername("user1");

        //we expect the obtained user to be the same as the sample user
        assertEquals(sampleUser, obtainedUser);

        //next, handle the case where the user does not exist in databae
        //tell repo to return empty optional when searching for user2
        when(userRepo.findByUsername("user2")).thenReturn(Optional.empty());

        //the user returned when the service searches for user2
        User obtainedUserTwo = userService.getUserByUsername("user2");

        //we expect return to be null
        assertEquals(null, obtainedUserTwo);
    }


}
