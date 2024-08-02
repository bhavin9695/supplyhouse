package com.supplyhouse.assignment.service;

import com.supplyhouse.assignment.model.DTO.LoginDTO;
import com.supplyhouse.assignment.model.entity.Account;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.security.sasl.AuthenticationException;
import java.util.Base64;

import static com.supplyhouse.assignment.config.Constants.AUTH_COOKIE_NAME;

/**
 * NOTE: For now, I am assuming authentication is taken care.
 * So will just return static value from this service.
 * This service can be improved in case of actual implementation.
 * */
@Service
public class AuthenticationService {


    private final AccountService accountServiceImpl;

    private static String loggedInUserAccount;


    public AuthenticationService(AccountService accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public String doLogin(LoginDTO loginDTO) throws AuthenticationException {
        Account account = accountServiceImpl.getAccountDetail(loginDTO.getLoginByAccountId());
        if(account == null){
            throw new AuthenticationException("Account not found");
        }
        loggedInUserAccount = Base64.getEncoder().encodeToString(loginDTO.getLoginByAccountId().getBytes());

        return "LoggedIn successful for account id : "+ loginDTO.getLoginByAccountId();
    }

    public String getLoggedInUserAccountId() {
        return new String(Base64.getDecoder().decode(loggedInUserAccount));
    }
}
