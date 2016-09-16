package com.project.web.service;


import com.project.services.CatalogService;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import com.project.services.UserService;

@ManagedBean(eager = false, name = "applicationManager")
@ApplicationScoped()
public class ApplicationManager implements Serializable {

    @ManagedProperty("#{userService}")
    private UserService userService = null;   

    @ManagedProperty("#{catalogService}")
    private CatalogService catalogService = null;

    public ApplicationManager() {
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

 

    public CatalogService getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

}
