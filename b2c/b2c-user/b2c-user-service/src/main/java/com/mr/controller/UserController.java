package com.mr.controller;

import com.mr.bo.User;
import com.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> check(
            @PathVariable(value = "data") String data,
            @PathVariable(value = "type") Integer type
    ){

        if(data == null && type == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean aBoolean = userService.validDataAndType(data, type);

        return ResponseEntity.ok(aBoolean);
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(User user){
        try {
            userService.register(user);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("query")
    public ResponseEntity<User> query(
            @RequestParam("username") String username ,
            @RequestParam("password") String password
    ){
        try {
            User u=userService.query(username,password);
            if(u == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }else{
                return ResponseEntity.ok(u);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
