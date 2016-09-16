package com.project.web.auth;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author home
 */
@ManagedBean(name = "userLogout")
@RequestScoped
public class UserLogout implements Serializable {

    private FacesContext context = null;
    private ExternalContext externalContext = null;

    public UserLogout() {
    }

    public String doLogout() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        //  externalContext.getSessionMap().remove("userContext");

        String localeCode = (String) externalContext.getSessionMap().get("localeCode");
        //System.out.println("localcode " + localeCode);
        if (localeCode != null) {
            context.getViewRoot().setLocale(new Locale(localeCode));
            String viewId = context.getViewRoot().getViewId();
        }
        externalContext.getSessionMap().remove("sessionContext");

        HttpSession session = (HttpSession) externalContext.getSession(true);
        session.invalidate();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "logout";
    }
}
