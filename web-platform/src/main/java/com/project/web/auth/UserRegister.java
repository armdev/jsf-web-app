package com.project.web.auth;

import com.project.entities.User;

import com.project.web.service.ApplicationManager;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@ManagedBean(name = "userRegister")
@ViewScoped
public class UserRegister implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{applicationManager}")
    private ApplicationManager applicationManager = null;
    private Long userCount;
    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    private User user = new User();
    private boolean agree;

    public UserRegister() {
        user = new User();
    }

    @PostConstruct
    public void init() {

    }


    public String saveUser() {
        User u = applicationManager.getUserService().getByEmail(user.getEmail());
        if (u != null) {
            FacesMessage msg = new FacesMessage(bundle.getString("emailbusy"), bundle.getString("emailbusy"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        } else {
            applicationManager.getUserService().save(user);
            return "success?faces-redirect=true";
        }

    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

}
