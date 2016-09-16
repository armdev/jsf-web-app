package com.project.web.management;

import com.project.entities.User;

import com.project.web.handlers.SessionContext;

import com.project.web.service.ApplicationManager;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.event.map.GeocodeEvent;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;

import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Home
 */
@ManagedBean
@ViewScoped
public class ProfileMapBean implements Serializable {

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
    private MapModel geoModel;
    private String centerGeoMap = "40.1833, 44.5167";

    public ProfileMapBean() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        geoModel = new DefaultMapModel();

        // System.out.println("User id received " + userId);
        user = applicationManager.getUserService().getById(sessionContext.getUser().getId());

        if (user != null) {

            if (user.getLat() != null && user.getLng() != null && user.getAddress() != null) {
                LatLng coord1 = new LatLng(user.getLat(), user.getLng());
                geoModel.addOverlay(new Marker(coord1, user.getAddress()));
            }
        } else {
            user = new User();
        }

    }

    public void onGeocode(GeocodeEvent event) {
        List<GeocodeResult> results = event.getResults();
        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            centerGeoMap = center.getLat() + "," + center.getLng();
            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                if (result.getAddress().contains("Yerevan")) {
                    // System.out.println("result.getAddress() " + result.getAddress());
                    sessionContext.setLat(result.getLatLng().getLat());
                    sessionContext.setLng(result.getLatLng().getLng());
                    sessionContext.setAddress(result.getAddress());
                    if (result.getLatLng() != null && result.getAddress() != null) {
                        geoModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
                        //this.update();
                    }
                }
            }
        }
    }

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String updateDetails() {
        try {
            user.setAddress(sessionContext.getAddress());
            user.setLat(sessionContext.getLat());
            user.setLng(sessionContext.getLng());
            applicationManager.getUserService().update(user);
            FacesMessage msg = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "profilemap";
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

    public MapModel getGeoModel() {
        return geoModel;
    }

    public void setGeoModel(MapModel geoModel) {
        this.geoModel = geoModel;
    }

    public String getCenterGeoMap() {
        return centerGeoMap;
    }

    public void setCenterGeoMap(String centerGeoMap) {
        this.centerGeoMap = centerGeoMap;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

}
