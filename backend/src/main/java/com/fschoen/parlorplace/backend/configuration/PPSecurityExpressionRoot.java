package com.fschoen.parlorplace.backend.configuration;

import com.fschoen.parlorplace.backend.service.UserService;
import lombok.Setter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class PPSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    @Setter
    private UserService userService;

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public PPSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean principalHasId(Long id) {
        return userService.getCurrentUser().getId().equals(id);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }


    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

}
