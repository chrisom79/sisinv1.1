package com.chrisom.sisinv;

import javax.servlet.annotation.WebServlet;

import com.chrisom.sisinv.model.VendedorModel;
import com.chrisom.sisinv.ui.Menu;
import com.chrisom.sisinv.ui.UIConstants;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
@Theme("valo") 
public class SisinvUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SisinvUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final HorizontalLayout layout = new HorizontalLayout();
		Window login = new Window(UIConstants.LOGIN_USUARIO);
		VerticalLayout content = new VerticalLayout();
		
		content.setMargin(true);
		TextField name = new TextField(UIConstants.LOGIN_NAME);
		PasswordField password = new PasswordField(UIConstants.LOGIN_PASSWORD);
		name.setValue("chrisom");
		password.setValue("password");
		
		Button button = new Button(UIConstants.BUTTON_ACCEDER);
	
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				VendedorModel model = new VendedorModel();
				if(model.isLoginSuccess(name.getValue().trim(), password.getValue().trim())) {
					login.close();
				} else {
					Notification.show("Credenciales incorrectas",
			                  "Favor de ingresar el user y password correctos",
			                  Notification.Type.ERROR_MESSAGE);
				}
				
			}
		});
		
		content.addComponent(name);
		content.addComponent(password);
		content.addComponent(button);
		content.setSpacing(true);
		
		login.setContent(content);
		login.center();
		login.removeCloseShortcut();
		login.setClosable(false);
		login.setModal(true);
		
		addWindow(login);
		
		Menu mainMenu = new Menu();
		layout.setMargin(true);
		setContent(layout);

		
		layout.addComponent(mainMenu.init(getUI()));
	}

}