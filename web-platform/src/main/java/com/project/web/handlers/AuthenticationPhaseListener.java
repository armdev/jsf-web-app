package com.project.web.handlers;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.apache.log4j.Logger;

public class AuthenticationPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;
    private static HashMap<String, String> pagePermissionMapping = null;
    Logger log = Logger.getLogger(AuthenticationPhaseListener.class);

    private void pagePermissionMapping() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (pagePermissionMapping == null) {
            pagePermissionMapping = new HashMap();
            try {
                ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "accessProp");
                if (bundle != null) {
                    Enumeration e = bundle.getKeys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = bundle.getString(key);
                        pagePermissionMapping.put(key, value);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public synchronized void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        ExternalContext ex = context.getExternalContext();
        try {
            pagePermissionMapping();

            String viewId = "/index.xhtml";

            if (context.getViewRoot() != null && context.getViewRoot().getViewId() != null) {
                viewId = context.getViewRoot().getViewId();

            }
            String permissions = pagePermissionMapping.get(viewId);
            SessionContext sessionContext = (SessionContext) ex.getSessionMap().get("sessionContext");

            String localeCode = (String) ex.getSessionMap().get("localeCode");
//System.out.println("Locale code " + localeCode);
            if (localeCode != null) {
                ex.getSessionMap().put("localeCode", localeCode);
                context.getViewRoot().setLocale(new Locale(localeCode));
            }

            if (sessionContext == null && !viewId.contains("index.xhtml") && !viewId.contains("public.xhtml") 
                    && !viewId.contains("register.xhtml") && !viewId.contains("about.xhtml") && !viewId.contains("agreement.xhtml")) {
              //  redirect(context, "../index.jsf?sessionExpired");
            }

            if (permissions != null && permissions.contains("PUBLIC")) {
                return;
            }
            if (permissions != null && sessionContext.getUser().getId() == null && permissions.contains("LOGGED") && !viewId.contains("index.xhtml")) {
                redirect(context, "../../index.jsf?illegalAccess");
            }

        } catch (Exception ex1) {
            redirect(context, "../../index.jsf?illegalAccess");
            ex1.printStackTrace();
        }
    }

    private static void redirect(FacesContext facesContext, String url) {
        try {
            facesContext.getExternalContext().redirect(url);
        } catch (IOException e) {
            throw new FacesException("Cannot redirect to " + url + " due to IO exception.", e);
        }
    }

    public void memoryStatus() {
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        //sb.append("Free memory: " + format.format(freeMemory / 1024 / 1024) + "<br/>");
        //sb.append("Allocated memory: " + format.format(allocatedMemory / 1024 / 1024) + "<br/>");
        //sb.append("Max memory: " + format.format(maxMemory / 1024 / 1024) + "<br/>");
        //sb.append("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024 / 1024) + "<br/>");
        System.out.println("AvailableProcessors " + runtime.availableProcessors());

        System.out.println("Free memory: " + format.format(freeMemory / 1024 / 1024) + " MB");
        System.out.println("Allocated memory: " + format.format(allocatedMemory / 1024 / 1024) + " MB");
        System.out.println("Max memory: " + format.format(maxMemory / 1024 / 1024) + " MB");
        System.out.println("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024 / 1024) + " MB");
        System.out.println("___________________________________________________");
        long free = freeMemory / 1024 / 1024;
        // if (free < 670) {
        // System.out.println("Calling GC ");
        //  System.out.println("Memory before  " + free);
        //  runtime.gc();
        // System.out.println("memory after  " + free);
        //}
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
