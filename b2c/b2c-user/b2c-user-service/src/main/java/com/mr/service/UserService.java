package com.mr.service;

import com.mr.mapper.UserMapper;
import com.mr.bo.User;
import com.mr.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean validDataAndType(String data, Integer type){

        User user = new User();

        if(type == 1){
            user.setUsername(data);
        }else if(type == 2){
            user.setPhone(data);
        }

        return userMapper.selectCount(user) == 0;
    }

    public void register(User user) {

        user.setCreated(new Date());
        user.setSalt(Md5Utils.generateSalt());
        user.setPassword(Md5Utils.md5Hex(user.getPassword(),user.getSalt()));

        userMapper.insert(user);
    }

    public User query(String username ,String password) {

        User user = new User();
        user.setUsername(username);

        // 根据用户名 username 查出来 判断
        User selectOne = userMapper.selectOne(user);
        if(selectOne == null){
            // 用户不存在
            return null;
        }else if(selectOne.getPassword().equals(Md5Utils.md5Hex(password,selectOne.getSalt()))){
            // 用户存在 密码正确
            selectOne.setPassword(null);
            selectOne.setSalt(null);
            return selectOne;
        }else{
            // 用户存在 密码不正确
            return null;
        }
    }
}
