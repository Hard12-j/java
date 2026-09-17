package org.example.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class userService {

    @Getter
    @Setter
    @AllArgsConstructor
    public class user{
        private String name;
        private int age;
        private String address;
    }

    private user user;

    public userService(){
        user = new user("hardik joshi", 23, "Indore, India");
    }

    public void login(){
        System.out.println("login user in");
    }

    public void logout() throws  Exception{
        System.out.println("logout user out");
        throw new Exception("unable to logout the user");
    }
}
