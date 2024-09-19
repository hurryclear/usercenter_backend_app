package com.hurryclear.usercenter_backend_app.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * request body for user register
 */
@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6018562549529666914L;

    private String userAccount;
    private String userPassword;
}
