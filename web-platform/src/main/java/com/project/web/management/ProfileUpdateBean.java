package com.project.web.management;

import com.project.entities.User;

import com.project.web.handlers.SessionContext;

import com.project.web.service.ApplicationManager;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Home
 */
@ManagedBean(name = "profileUpdateBean")
@ViewScoped
public class ProfileUpdateBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{applicationManager}")
    private ApplicationManager applicationManager = null;
    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    private User user = new User();
    @ManagedProperty("#{sessionContext}")
    private SessionContext sessionContext = null;
    private Long userId;
    private FacesContext context = null;
    private ExternalContext externalContext = null;

    public ProfileUpdateBean() {

    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        user = applicationManager.getUserService().getById(sessionContext.getUser().getId());

        if (user == null) {
            user = new User();
        }

    }

    public String updateUser() {

        boolean checkEmail = applicationManager.getUserService().checkUserEmailForUpdate(userId, user.getEmail());
        if (!checkEmail) {
            applicationManager.getUserService().update(user);
            FacesMessage msg = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(bundle.getString("emailbusy"), bundle.getString("emailbusy"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

        return null;
    }

    public String updateDetails() {
        try {

            applicationManager.getUserService().update(user);
            FacesMessage msg = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private String getRequestParameter(String paramName) {
        String returnValue = null;
        if (externalContext.getRequestParameterMap().containsKey(paramName)) {
            returnValue = (externalContext.getRequestParameterMap().get(paramName));
        }
        return returnValue;
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
