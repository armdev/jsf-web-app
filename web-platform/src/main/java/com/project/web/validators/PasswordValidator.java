package com.project.web.validators;

import com.project.web.utils.MessageFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.myfaces.component.visit.FullVisitContext;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String passwordId = (String) component.getAttributes().get("passwordId");
       // System.out.println("passwordId " + passwordId);
        UIInput passwordInput = (UIInput) this.findComponent(passwordId);
        String password = null;
        //System.out.println("passwordInput " + passwordInput);
        if (passwordInput != null) {
            password = (String) passwordInput.getValue();
        }
        String confirm = (String) value;
       // System.out.println("confirm " + confirm);
        if (password != null && password.length() != 0 && !password.equals(confirm)) {
            MessageFactory msg = new MessageFactory();
            FacesMessage errMsg = new FacesMessage(msg.getMessage("errorPasswordConfirm"));
            throw new ValidatorException(errMsg);

        }
    }

    public UIComponent findComponent(final String id) {

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        final UIComponent[] found = new UIComponent[1];

        root.visitTree(new FullVisitContext(context), new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent component) {
                if (component.getId().equals(id)) {
                    found[0] = component;
                    return VisitResult.COMPLETE;
                }
                return VisitResult.ACCEPT;
            }
        });

        return found[0];

    }
}
