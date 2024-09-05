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
}
