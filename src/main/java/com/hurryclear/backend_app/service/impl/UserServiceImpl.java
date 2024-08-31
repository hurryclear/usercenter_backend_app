package com.hurryclear.backend_app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hurryclear.backend_app.model.domain.User;
import com.hurryclear.backend_app.service.UserService;
import com.hurryclear.backend_app.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author hurjiang
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-08-30 18:23:02
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




