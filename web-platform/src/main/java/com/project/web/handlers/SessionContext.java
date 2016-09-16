package com.project.web.handlers;

import com.project.entities.User;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author home
 */
@ManagedBean(name = "sessionContext")
@SessionScoped
public class SessionContext implements Serializable {

    private final FacesContext context;
    private final ExternalContext ex;
    private User user;
    private Integer activeIndex = 0;
    private String address;
    private Double lat;
    private Double lng;
    private User editUser;
    private Long paramId;
    private String searchKey;

    public SessionContext() {
        context = FacesContext.getCurrentInstance();
        ex = context.getExternalContext();
        user = new User();
        editUser = new User();
    }

    public void onChange(TabChangeEvent event) {
        switch (event.getTab().getId()) {
            case "settings":
                activeIndex = 1;
                break;
            case "dataManagement":
                activeIndex = 0;
                break;
            case "statistic":
                activeIndex = 2;
                break;
            case "configs":
                activeIndex = 3;
                break;
            default:
                activeIndex = 4;
                break;
        }

    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public User getEditUser() {
        return editUser;
    }

    public void setEditUser(User editUser) {
        this.editUser = editUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(Integer activeIndex) {
        this.activeIndex = activeIndex;
    }

}
