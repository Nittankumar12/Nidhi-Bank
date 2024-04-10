package com.Railworld.NidhiBankMonolithic.controller;

import com.Railworld.NidhiBankMonolithic.dto.MemberDto;
import com.Railworld.NidhiBankMonolithic.dto.UserDto;
import com.Railworld.NidhiBankMonolithic.model.Member;
import com.Railworld.NidhiBankMonolithic.model.User;
import com.Railworld.NidhiBankMonolithic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.register(userDto) , HttpStatus.CREATED);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id){
        User user = userService.getUser(id);
        if(user == null) new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        assert user != null;
        System.out.println(user.getCompany().getCName());
        System.out.println(user.isJoined());
        System.out.println(user.getUId());
        System.out.println(user.getUEmail());
        System.out.println(user.getUPassword());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("JoinBank/{id}")
    public ResponseEntity<String> joinBank(@PathVariable Integer id,@RequestBody MemberDto memberDto){
        Integer memberId = userService.joinBank(memberDto);
        userService.updateMember(memberId,id);
        return new ResponseEntity<>("Congratulations",HttpStatus.CREATED);
    }

}
