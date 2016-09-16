package com.project.web.management;

import com.project.entities.User;

import com.project.web.handlers.SessionContext;

import com.project.web.utils.ParamUtil;

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
import com.project.services.UserService;

/**
 *
 * @author Home
 */
@ManagedBean(name = "updateMapBean")
@ViewScoped
public class UpdateMapBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    private User user = null;
    @ManagedProperty("#{sellerService}")
    private UserService sellerService;
    @ManagedProperty("#{sessionContext}")
    private SessionContext sessionContext = null;
    private Long userId;
    private FacesContext context = null;
    private ExternalContext externalContext = null;
    private MapModel geoModel;
    private String centerGeoMap = "40.1833, 44.5167";

    public UpdateMapBean() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();

    }

    @PostConstruct
    public void init() {
        geoModel = new DefaultMapModel();
        userId = ParamUtil.longValue((this.getRequestParameter("userId")));

       // System.out.println("User id received " + userId);
        if (userId != null) {
          //  System.out.println("sellerService " + sellerService);
            user = sellerService.getById(userId);
            sessionContext.setParamId(userId);
        } else {
            user = sellerService.getById(sessionContext.getParamId());
        }

        if (user != null) {
          //  System.out.println("User is not null ");
            if (user.getLat() != null && user.getLng() != null && user.getAddress() != null) {
              //  System.out.println("Setting geo model ");
                LatLng coord1 = new LatLng(user.getLat(), user.getLng());
                geoModel.addOverlay(new Marker(coord1, user.getAddress()));
            }
        } else {
            user = new User();
        }

    }

    public void onGeocode(GeocodeEvent event) {
        //System.out.println("onGeocode called ");
        List<GeocodeResult> results = event.getResults();
        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            centerGeoMap = center.getLat() + "," + center.getLng();
            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                //System.out.println("result.getAddress() one:  " + result.getAddress());
                if (result.getAddress().contains("Yerevan") && result.getAddress().contains("Armenia")) {
                    //System.out.println("result.getAddress() " + result.getAddress());
                    sessionContext.setLat(result.getLatLng().getLat());
                    sessionContext.setLng(result.getLatLng().getLng());
                    sessionContext.setAddress(result.getAddress());
                    if (result.getLatLng() != null && result.getAddress() != null) {
                        geoModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
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
            user.setId(userId != null ? userId : sessionContext.getParamId());
           // System.out.println("Saving with address " + user.getAddress());
            user.setAddress(sessionContext.getAddress());
            user.setLat(sessionContext.getLat());
            user.setLng(sessionContext.getLng());
            sellerService.update(user);

            FacesMessage msg = new FacesMessage(bundle.getString("success"), bundle.getString("success"));
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "updatemap?faces-redirect=true&userId=" + sessionContext.getParamId();
        //return null;
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

    public void setSellerService(UserService sellerService) {
        this.sellerService = sellerService;
    }

}
