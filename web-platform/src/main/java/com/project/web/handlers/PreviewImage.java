package com.project.web.handlers;

import com.project.entities.Catalog;
import com.project.web.service.ApplicationManager;
import com.project.web.utils.FacesUtil;
import com.project.web.utils.ParamUtil;

import java.io.*;

import javax.faces.context.FacesContext;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author home
 */
@WebServlet(urlPatterns = {"/PreviewImage"})
public class PreviewImage extends HttpServlet implements SingleThreadModel {

    private static final long serialVersionUID = -6624464650990859671L;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPreviewImage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private void doPreviewImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FacesContext context = FacesUtil.getFacesContext(request, response);
        ApplicationManager appManager = context.getApplication().evaluateExpressionGet(
                context, "#{applicationManager}", ApplicationManager.class);

        String fileIdStr = request.getParameter("fileId");
        String widthStr = request.getParameter("w");
        int width = 0;
        if ((widthStr != null) && (widthStr.length() > 0)) {
            try {
                width = Integer.parseInt(widthStr);
            } catch (NumberFormatException e) {
            }
        }
        if (fileIdStr != null) {
            Catalog file = appManager.getCatalogService().findById(ParamUtil.longValue(fileIdStr));
            if (file != null) {
                byte[] content = file.getImage();
                if (content != null) {
                    response.addHeader("Pragma", "cache");
                    response.addHeader("Cache-Control", "max-age=3600, must-revalidate");
                    response.addDateHeader("Expires", System.currentTimeMillis() + 1000 * 3600 * 10);
                    response.setContentType("image/png");
                    try {
                        response.getOutputStream().write(content);
                    } catch (IOException e) {

                    } catch (Exception e) {

                    } finally {
                        content = null;
                        file = null;
                    }
                    return;
                }
            } else {
                response.addHeader("Pragma", "no-cache");
                response.addDateHeader("Expires", System.currentTimeMillis() - 1000 * 3600);
                try {
                    response.getWriter().println("file object is null");
                } catch (Exception e) {
                }
                return;
            }
        }
        response.addHeader("Pragma", "no-cache");
        response.addDateHeader("Expires", System.currentTimeMillis() - 1000 * 3600);
        try {
            response.getWriter().println("file id is not set");
        } catch (Exception e) {
        }
    }
}
