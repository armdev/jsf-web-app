package com.project.web.auth;

import com.project.entities.User;
import com.project.utils.CommonConstants;
import com.project.web.service.ApplicationManager;
import com.project.web.handlers.SessionContext;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author home
 */
@ManagedBean(name = "userLogin")
@RequestScoped
public class UserLogin {

    @ManagedProperty("#{applicationManager}")
    private ApplicationManager applicationManager = null;

    private String email;
    private String password;
    private Boolean rememberMe;
    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    @ManagedProperty("#{sessionContext}")
    private SessionContext sessionContext = null;

    /**
     * Creates a new instance of UserLogin
     */
    public UserLogin() {
    }

    public String loginUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ex = context.getExternalContext();
        HttpServletRequest httpServletRequest = (HttpServletRequest) ex.getRequest();
        String ip = httpServletRequest.getRemoteAddr();        
        User user = applicationManager.getUserService().userLogin(email, password, ip);
        if (user != null && user.getStatus().equals(CommonConstants.ACTIVATED)) {
            sessionContext.setUser(user);
            return "profile";
        } else {
            FacesMessage msg = new FacesMessage(bundle.getString("nouser"), bundle.getString("nouser"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }

    }

    private void facesError(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

}
