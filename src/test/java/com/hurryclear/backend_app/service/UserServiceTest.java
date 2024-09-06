package com.hurryclear.backend_app.service;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.hurryclear.backend_app.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * user service test
 * @author hurryclear
 */

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUserAccount("hurryclear");
        user.setUserPassword("123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setEmail("111");
        user.setPhone("2323");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    // how to write unit test? how to debug?
    @Test
    void userRegister() {

        String userAccount = "hurryclear";
        String checkPassword = "123456";
        // test not empty
        String userPassword = "";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        // test username not shorter than 4
        userAccount = "hh";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        // test password is not shorter than 8
        userAccount = "hurryclear";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        // test no special ziffer
        userAccount = "hurry clear";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        // test password and check password are same
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        // test no two same user accounts
        userAccount = "hurryclear1";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        // test the successful registration
        userAccount = "hurjiang";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);

    }
}
