package com.project.web.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author home
 */
@FacesConverter("timeConverter")
public class TimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        //System.out.println("Time converter start");
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date nDate = null;
        if (value == null || value.trim().length() == 0) {
            return nDate;
        }
        try {
            nDate = sdf.parse(value);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return value;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object value) {
        // System.out.println("Time converter start 1");
//String pattern = "yyyy/MM/dd kk:mm:ss";
        //  System.out.println("Value " + value);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4"));
        //Long time = Long.valueOf(value.toString());
        //Date dvalue = new Date(time);
        String date = sdf.format((Date) value);
        return date;
    }

}
