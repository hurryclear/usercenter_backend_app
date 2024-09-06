package com.hurryclear.backend_app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hurryclear.backend_app.model.domain.User;
import com.hurryclear.backend_app.service.UserService;
import com.hurryclear.backend_app.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
@Slf4j // log?
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper; // insert mapper ?? service call mapper

    private static final String SALT = "hryclr";

    /**
     * key of user login's status
     */
    private static final String USER_LOGIN_STATE = "userLoginState";

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
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if  (matcher.find()) {
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

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        // 1. check the validation of username and user_password

        // 1.1 no empty user account/password/check_password
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        // account is not shorter than 4
        if(userAccount.length() < 4) {
            return null;
        }

        // password is not shorter than 8
        if(userPassword.length() < 8) {
            return null;
        }

        // 1.2 username is not allowed to include special ziffer
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if  (matcher.find()) {
            return null;
        }

        // 2. password encrypt
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. search the account with the password in database to if there is such a user
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper); // ???
        // if there is no such a user
        if(user == null) {
            log.info("user login failed, userAccount cannot match userPassword"); // what is log? why do I need this?
            return null;
        }

        // 4. get rid of the sensitive information of user
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());

        // 5. record user's status of login
        // after successful login: a hashmap will be put into attributes under session (use debug can easily see this)
        // but i'm not sure about the purpose
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser); // ???

        return safetyUser;
    }
}

