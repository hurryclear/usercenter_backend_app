package com.hurryclear.usercenter_backend_app.service;

import com.hurryclear.usercenter_backend_app.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author hurryclear
* @description 针对表【user】的数据库操作Service
* @createDate 2024-08-30 18:23:02
*/
public interface UserService extends IService<User> {

    /**
     * user register
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return user_Id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * user login
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return user info without sensitiv info
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);
    // what is HttpServletRequest request?

    /**
     * get rid of the sensitive information of user
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
}
