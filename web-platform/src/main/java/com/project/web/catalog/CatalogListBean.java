package com.project.web.catalog;

import com.project.entities.Catalog;
import com.project.web.handlers.SessionContext;
import com.project.web.service.ApplicationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

@ManagedBean(name = "catalogListBean")
@ViewScoped
public class CatalogListBean implements Serializable {

    private static final Logger log = Logger.getLogger(CatalogListBean.class);
    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{applicationManager}")
    private ApplicationManager applicationManager = null;
    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    @ManagedProperty("#{sessionContext}")
    private SessionContext sessionContext = null;
    private Part uploadedFile = null;    
    private Catalog catalog = null;
    private FacesContext context = null;
    private ExternalContext externalContext = null;

    public CatalogListBean() {

    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        catalog = new Catalog();

    }
    
     public List<Catalog> getCatalogList(){
         return applicationManager.getCatalogService().getCatalogList();
     }
     
     public void delete(Long id){
         applicationManager.getCatalogService().destroy(id);
     }

 

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void facesError(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

}
