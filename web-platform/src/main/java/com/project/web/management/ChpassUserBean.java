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
@ManagedBean(name = "chpassUserBean")
@ViewScoped
public class ChpassUserBean implements Serializable {

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
    private String passwd;

    public ChpassUserBean() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();

    }

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String chpass() {
        applicationManager.getUserService().updatePassword(sessionContext.getUser().getId(), passwd);
        FacesMessage msg = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "settings";
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

}
