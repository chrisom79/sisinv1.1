package com.chrisom.sisinv.ui;

import java.util.List;

import com.chrisom.sisinv.entity.Vendedor;
import com.chrisom.sisinv.model.VendedorModel;
import com.chrisom.sisinv.utils.Algorithms;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class VendedorUI {
	TextField nombreLbl = null;
	TextField direccionLbl = null;
	TextField telefonoLbl = null;
	TextField usuarioLbl = null;
	TextField nombreSrch = null;
	TextField telefonoSrch = null;
	TextField usuarioSrch = null;
	
	public Window createWindow() {
		Window vendedorW = new Window(UIConstants.CREAR_USUARIO_ITEM);
		VerticalLayout content = new VerticalLayout();
		HorizontalLayout subcontent = new HorizontalLayout();
		content.setMargin(true);
		vendedorW.setContent(content);
		
		nombreLbl = new TextField(UIConstants.VENDEDOR_NOMBRE);
		direccionLbl = new TextField(UIConstants.VENDEDOR_DIRECCION);
		telefonoLbl = new TextField(UIConstants.VENDEDOR_TELEFONO);
		usuarioLbl = new TextField(UIConstants.VENDEDOR_USUARIO);
		
		nombreLbl.setWidth("500");
		direccionLbl.setWidth("500");
		
		content.addComponent(nombreLbl);
		content.addComponent(direccionLbl);
		content.addComponent(telefonoLbl);
		content.addComponent(usuarioLbl);
		
		createValidators();
		Button guardarBtn = createGuardarButton();
		
		subcontent.addComponent(guardarBtn);
		
		content.setSpacing(true);
		subcontent.setSpacing(true);
		content.addComponent(subcontent);
		
		vendedorW.center();
		
		return vendedorW;
	}
	
	@SuppressWarnings("deprecation")
	public Window createWindowSrch(){
		Window vendedorPnl = new Window(UIConstants.BUSCAR_USUARIO_ITEM);
		VerticalLayout content =  new VerticalLayout();
		HorizontalLayout subcontent1 = new HorizontalLayout();
		HorizontalLayout subcontent2 = new HorizontalLayout();
		IndexedContainer container= createResultContainer();
		
		content.setMargin(true);
		content.setSizeUndefined();
		
		nombreSrch = new TextField(UIConstants.VENDEDOR_NOMBRE);
		telefonoSrch = new TextField(UIConstants.VENDEDOR_TELEFONO);
		usuarioSrch = new TextField(UIConstants.VENDEDOR_USUARIO);
		
		Table result = new Table();
		Button buscarBtn = createSearchButton(container, result) ;
		
		subcontent1.addComponent(nombreSrch);
		subcontent1.addComponent(telefonoSrch);
		subcontent1.addComponent(usuarioSrch);
		subcontent2.addComponent(buscarBtn);
		content.addComponent(subcontent1);
		content.addComponent(subcontent2);
		result.setContainerDataSource(container);
		result.setHeight(5, Sizeable.UNITS_CM);
		result.setWidth(20, Sizeable.UNITS_CM);
		result.setPageLength(10);
		result.setSelectable(true);
		
		content.addComponent(result);
		
		content.setSpacing(true);
		subcontent1.setSpacing(true);
		vendedorPnl.setContent(content);
		vendedorPnl.center();
		
		return vendedorPnl;
	}
	
	private Window createWindowEdit(String usuario) {
		Window window = new Window(UIConstants.EDITAR_USUARIO_ITEM);
		VerticalLayout container = new VerticalLayout();
		nombreLbl = new TextField(UIConstants.VENDEDOR_NOMBRE);
		direccionLbl = new TextField(UIConstants.VENDEDOR_DIRECCION);
		telefonoLbl = new TextField(UIConstants.VENDEDOR_TELEFONO);
		usuarioLbl = new TextField(UIConstants.VENDEDOR_USUARIO);
		
		nombreLbl.setWidth("500");
		direccionLbl.setWidth("500");
		
		container.setSpacing(true);
		container.setMargin(true);
		if(usuario != null && !usuario.isEmpty()) {
			VendedorModel model = new VendedorModel();
			Vendedor vendedor = model.findVendedorByUsuario(usuario);
			if(vendedor != null) {
				nombreLbl.setValue(vendedor.getNombre());
				direccionLbl.setValue(vendedor.getDireccion());
				telefonoLbl.setValue(vendedor.getTelefono());
				usuarioLbl.setValue(vendedor.getUsuario());
			}
		}
		
		container.addComponent(nombreLbl);
		container.addComponent(direccionLbl);
		container.addComponent(telefonoLbl);
		container.addComponent(usuarioLbl);
		
		window.setContent(container);
		
		window.center();
		return window;
	}
	
	private IndexedContainer createResultContainer() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(UIConstants.VENDEDOR_NOMBRE, String.class, null);
		container.addContainerProperty(UIConstants.VENDEDOR_DIRECCION, String.class, null);
		container.addContainerProperty(UIConstants.VENDEDOR_TELEFONO, String.class, null);
		container.addContainerProperty(UIConstants.VENDEDOR_USUARIO, String.class, null);
		container.addContainerProperty(UIConstants.LAUNCH_WINDOW, Button.class, null);
		container.addContainerProperty(UIConstants.REMOVE, Button.class, null);
		return container;
	}
	
	private Button createGuardarButton() {
		Button button = new Button(UIConstants.BUTTON_GUARDAR);
		VendedorModel model = new VendedorModel();
	
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(!model.existsUsername(usuarioLbl.getValue())) {
					Vendedor vendedor = new Vendedor(model.createId(nombreLbl.getValue()), nombreLbl.getValue(),
							telefonoLbl.getValue(), usuarioLbl.getValue(), Algorithms.encryptMD5("password"));
					vendedor.setDireccion(direccionLbl.getValue());
					model.insertVendedor(vendedor);
					
					Window window = (Window) direccionLbl.getParent().getParent();
					window.close();
				} else {
					Notification.show("Nombre de usuario existente",
			                  "Favor de ingresar un nombre de usuario diferente",
			                  Notification.Type.WARNING_MESSAGE);
				}
			}
		});
		
		return button;
	}
	
	
	
	private Button createSearchButton(IndexedContainer container, Table table) {
		Button button = new Button(UIConstants.BUTTON_BUSCAR);
		VendedorModel model = new VendedorModel();
	
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				List<Vendedor> vendedores = model.findVendedoresByParameters(nombreSrch.getValue(), 
						telefonoSrch.getValue(), usuarioSrch.getValue());
				container.removeAllItems();
				for(Vendedor vendedor : vendedores) {
					Object id = container.addItem();
					Item item = container.getItem(id);
					item.getItemProperty(UIConstants.VENDEDOR_NOMBRE).setValue(vendedor.getNombre());
					item.getItemProperty(UIConstants.VENDEDOR_DIRECCION).setValue(vendedor.getDireccion());
					item.getItemProperty(UIConstants.VENDEDOR_TELEFONO).setValue(vendedor.getTelefono());
					item.getItemProperty(UIConstants.VENDEDOR_USUARIO).setValue(vendedor.getUsuario());
					item.getItemProperty(UIConstants.LAUNCH_WINDOW).setValue(createLaunchButton((UI)table.getParent().getParent().getParent(), (Window)table.getParent().getParent(), vendedor.getUsuario()));
					item.getItemProperty(UIConstants.REMOVE).setValue(createRemoveButton(table, id, vendedor.getUsuario()));
				
				}
				
				table.refreshRowCache();
			}
		});
		
		return button;
	}
	
	private Button createLaunchButton(UI ui, Window current, String usuario) {
		Button button = new Button();
		button.setIcon(FontAwesome.EXTERNAL_LINK);
		button.setId(usuario);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				current.close();
				ui.addWindow(createWindowEdit(usuario));
				
			}
		});

		return button;
	}
	
	private Button createRemoveButton(Table table, Object id, String usuario) {
		Button button = new Button();
		button.setIcon(FontAwesome.TRASH_O);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				VendedorModel model = new VendedorModel();
				model.deleteByUsuario(usuario);
				table.removeItem(id);
				table.refreshRowCache();
			}
		});

		return button;
	}
	
	private void createValidators() {
		VendedorModel model = new VendedorModel();
		class UsuarioValidator implements Validator {

			private static final long serialVersionUID = -8961627409896728756L;

			@Override
		    public void validate(Object value)
		            throws InvalidValueException {
		        if (model.existsUsername(usuarioLbl.getValue()))
		            throw new InvalidValueException("Favor de ingresar otro nombre de usuario, el actual ya existe en el sistema");
		    }
		}
		
		usuarioLbl.addValidator(new UsuarioValidator());
		usuarioLbl.setImmediate(true);
		
	}
}
