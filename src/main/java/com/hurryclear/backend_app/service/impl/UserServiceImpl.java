package com.hurryclear.backend_app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hurryclear.backend_app.model.domain.User;
import com.hurryclear.backend_app.service.UserService;
import com.hurryclear.backend_app.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.NativeWebRequest;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * user service implement class, all write the main functional code here
* @author hurjiang
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-08-30 18:23:02
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper; // insert mapper ?? service call mapper

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        // 1. check the validation of username and user_password

        // 1.1 no empty user account/password/check_password
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }

        if(userAccount.length() < 4) {
            return -1;
        }

        if(userPassword.length() <8 || checkPassword.length() < 8) {
            return -1;
        }

        // 1.2 username is not allowed to include special ziffer
        String validPattern = "\\pP|\\pS|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if  (!matcher.find()) {
            return -1;
        }

        // 1.4 password and checked password are same
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 1.3 account is unique (put this step after the others is helpful to reduce the query to database)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }


        // 2. password encrypt
        final String SALT = "hurryclear";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. save new user into database
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {  // why do I need this? because our user Id is "Long" type. If return "null", will error. ??
            return -1;
        }
        return user.getId();

    }
}




