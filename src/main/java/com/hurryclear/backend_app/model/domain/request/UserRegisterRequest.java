package com.hurryclear.backend_app.model.domain.request;

import lombok.Data;
import org.apache.ibatis.javassist.SerialVersionUID;

import java.io.Serial;
import java.io.Serializable;


/**
 * request body for user register
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6018562549529666914L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
