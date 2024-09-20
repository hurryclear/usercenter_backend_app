package com.hurryclear.usercenter_backend_app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hurryclear.usercenter_backend_app.model.domain.User;
import com.hurryclear.usercenter_backend_app.model.domain.request.UserRegisterRequest;
import com.hurryclear.usercenter_backend_app.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hurryclear.usercenter_backend_app.constant.UserConstant.ADMIN_ROLE;
import static com.hurryclear.usercenter_backend_app.constant.UserConstant.USER_LOGIN_STATE;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    // what is difference "long" and "Long"?
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {

        if (userRegisterRequest == null) {
            return null;
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword);

    }

    @PostMapping("/login")
    public User userRegister(@RequestBody UserRegisterRequest userLoginRequest, HttpServletRequest request) {

        if (userLoginRequest == null) {
            return null;
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.userLogin(userAccount, userPassword, request);

    }

    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request){

        if (!isAdmin(request)) {
            return new ArrayList<>();
        }

        // what is QueryWrapper?
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }

        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {

        if (!isAdmin(request)) {
            return false;
        }

        if (id <= 0) {
            return false;
        }
        return userService.removeById(id); // logic delete
    }

    /**
     * is admin?
     * @param request
     * @return boolean
     */
    private boolean isAdmin (HttpServletRequest request) {
        // this interface is only for admin accessible, we have to determine the role
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            return false;
        }
        return true;
    }

    // test git

}
