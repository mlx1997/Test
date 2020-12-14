package com.mr.api;

import com.mr.bo.User;
import org.springframework.web.bind.annotation.*;

@RestController
public interface UserApi{

    @GetMapping("check/{data}/{type}")
    public Boolean check(
            @PathVariable(value = "data") String data,
            @PathVariable(value = "type") Integer type
    );

//    @PostMapping(value="register")
//    public Void register(User user);

    @GetMapping(value = "query")
    public User query(
            @RequestParam("username") String username ,
            @RequestParam("password") String password);
}
