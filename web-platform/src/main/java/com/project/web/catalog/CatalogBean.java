package com.project.web.catalog;

import com.project.entities.Catalog;
import com.project.web.handlers.SessionContext;
import com.project.web.service.ApplicationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

@ManagedBean(name = "catalogBean")
@ViewScoped
public class CatalogBean implements Serializable {

    private static final Logger log = Logger.getLogger(CatalogBean.class);
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

    public CatalogBean() {

    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
        catalog = new Catalog();

    }

    public String doAction() throws IOException {
        if (uploadedFile != null) {
            long size = uploadedFile.getSize();
            long maxSize = 5 * 1000000;
            if (size > maxSize) {
                facesError(bundle.getString("fileTooBig"));
                return null;
            }
            String content = uploadedFile.getContentType();
            
            
            InputStream stream = uploadedFile.getInputStream();
            
            byte[] contentBytes = new byte[(int) size];
            
            stream.read(contentBytes);
            catalog.setImage(contentBytes);
            
            if (!content.equalsIgnoreCase("image/jpeg") && !content.equalsIgnoreCase("image/pjpeg")
                    && !content.equalsIgnoreCase("image/jpg") && !content.equalsIgnoreCase("image/gif")
                    && !content.equalsIgnoreCase("image/x-png") && !content.equalsIgnoreCase("image/png")) {
                try {
                    facesError(bundle.getString("fileFormatError"));

                    return null;
                } catch (Exception e) {
                }
            }
            applicationManager.getCatalogService().save(catalog);
        }
        return "catalog";

    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
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
