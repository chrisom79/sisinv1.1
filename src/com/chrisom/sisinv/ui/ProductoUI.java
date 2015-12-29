package com.chrisom.sisinv.ui;

import java.util.List;

import com.chrisom.sisinv.entity.Producto;
import com.chrisom.sisinv.model.ProductoModel;
import com.chrisom.sisinv.model.VendedorModel;
import com.chrisom.sisinv.utils.ProductoUtils;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ProductoUI {
	TextField id = null;
	TextField nombre = null;
	TextField precioCompra = null;
	TextField porcentaje = null;
	CheckBox iva = null;
	TextField idSrch = null;
	
	public Window createWindowNew() {
		Window prodWindow = new Window(UIConstants.CREAR_PRODUCTO_ITEM);
		prodWindow.center();
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		
		id = new TextField(UIConstants.PRODUCTO_CODIGO);
		id.setWidth("300");
		nombre = new TextField(UIConstants.PRODUCTO_NOMBRE);
		nombre.setWidth("500");
		precioCompra = new TextField(UIConstants.PRODUCTO_PRECIO_COMPRA);
		porcentaje = new TextField(UIConstants.PRODUCTO_PORCENTAJE);
		iva = new CheckBox(UIConstants.PRODUCTO_IVA);
		Button saveButton = createSaveButton();
		
		content.addComponent(id);
		content.addComponent(nombre);
		content.addComponent(precioCompra);
		content.addComponent(porcentaje);
		content.addComponent(iva);
		content.addComponent(saveButton);
		
		content.setSpacing(true);
		prodWindow.setContent(content);
		return prodWindow;
	}
	
	public Window createWindowSrch() {
		Window prodWindow = new Window(UIConstants.BUSCAR_PRODUCTO_ITEM);
		IndexedContainer container= createResultContainer();
		Table result = new Table();
		
		VerticalLayout content =  new VerticalLayout();
		HorizontalLayout subcontent1 = new HorizontalLayout();
		HorizontalLayout subcontent2 = new HorizontalLayout();
		idSrch = createIdSearchField(container, result);
		
		subcontent1.addComponent(idSrch);
		subcontent2.addComponent(createSearchButton(container, result));
		
		result.setContainerDataSource(container);
		result.setHeight(5, Sizeable.UNITS_CM);
		result.setWidth(20, Sizeable.UNITS_CM);
		result.setPageLength(10);
		result.setSelectable(true);
		
		subcontent1.setSpacing(true);
		content.setSpacing(true);
		content.setMargin(true);
		
		idSrch.focus();
		content.addComponent(subcontent1);
		content.addComponent(subcontent2);
		content.addComponent(result);
		
		prodWindow.setContent(content);
		prodWindow.center();
		
		return prodWindow;
	}
	
	public Window createWindowEdit(String code) {
		
		Window prodWindow = new Window(UIConstants.EDITAR_PRODUCTO_ITEM);
		prodWindow.center();
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		
		id = new TextField(UIConstants.PRODUCTO_CODIGO);
		id.setWidth("300");
		nombre = new TextField(UIConstants.PRODUCTO_NOMBRE);
		nombre.setWidth("500");
		precioCompra = new TextField(UIConstants.PRODUCTO_PRECIO_COMPRA);
		porcentaje = new TextField(UIConstants.PRODUCTO_PORCENTAJE);
		iva = new CheckBox(UIConstants.PRODUCTO_IVA);
		Button saveButton = createSaveButton();
		
		if(code != null && !code.isEmpty()) {
			ProductoModel model = new ProductoModel();
			Producto producto = model.findProductoByCode(code);
			
			if(producto != null) {
				id.setValue(producto.getId());
				nombre.setValue(producto.getNombre());
				precioCompra.setValue(String.valueOf(producto.getPrecioCompra()));
				porcentaje.setValue(String.valueOf(producto.getPorcentaje()));
				iva.setValue(producto.getIva());
			}
		}
		
		content.addComponent(id);
		content.addComponent(nombre);
		content.addComponent(precioCompra);
		content.addComponent(porcentaje);
		content.addComponent(iva);
		content.addComponent(saveButton);
		
		content.setSpacing(true);
		prodWindow.setContent(content);
		return prodWindow;
	}
	private Button createSaveButton() {
		Button button = new Button(UIConstants.BUTTON_GUARDAR);
		ProductoModel model = new ProductoModel();
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Producto producto = new Producto(id.getValue(), nombre.getValue(), Double.parseDouble(precioCompra.getValue()), 
						Integer.parseInt(porcentaje.getValue()), iva.getValue());
				Window window = (Window) id.getParent().getParent();
				if(UIConstants.CREAR_PRODUCTO_ITEM.equalsIgnoreCase(window.getCaption()))
					model.insertProducto(producto);
				else
					model.updateProducto(producto);
					
				window.close();
				
			}
		});
		
		return button;
	}
	
	private Button createSearchButton(IndexedContainer container, Table table) {
		Button button = new Button(UIConstants.BUTTON_BUSCAR);
	
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				executeFindProductosByParameters(container, table);
			}
		});
		
		return button;
	}
	
	private IndexedContainer createResultContainer() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(UIConstants.PRODUCTO_NOMBRE, String.class, null);
		container.addContainerProperty(UIConstants.PRODUCTO_PRECIO_COMPRA, Double.class, null);
		container.addContainerProperty(UIConstants.PRODUCTO_PRECIO_VENTA, Double.class, null);
		container.addContainerProperty(UIConstants.PRODUCTO_IVA, String.class, null);
		container.addContainerProperty(UIConstants.LAUNCH_WINDOW, Button.class, null);
		container.addContainerProperty(UIConstants.REMOVE, Button.class, null);
		return container;
	}
	
	private TextField createIdSearchField(IndexedContainer container, Table table){
		TextField idSrch = new TextField();
		
		idSrch = new TextField(UIConstants.PRODUCTO_CODIGO);
		idSrch.setWidth("300");
		
		idSrch.addShortcutListener(new ShortcutListener("shortcutId", ShortcutAction.KeyCode.ENTER, null) {
		    @Override
		    public void handleAction(Object sender, Object target) {
		    	executeFindProductosByParameters(container, table);
		    }
		});
		
		return idSrch;
	}
	
	private Button createLaunchButton(UI ui, Window current, String id) {
		Button button = new Button();
		button.setIcon(FontAwesome.EXTERNAL_LINK);
		button.setId(id);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				current.close();
				ui.addWindow(createWindowEdit(id));
				
			}
		});

		return button;
	}
	
	private Button createRemoveButton(Table table, Object id, String code) {
		Button button = new Button();
		button.setIcon(FontAwesome.TRASH_O);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ProductoModel model = new ProductoModel();
				model.deleteById(code);
				table.removeItem(id);
				table.refreshRowCache();
			}
		});

		return button;
	}
	
	private void executeFindProductosByParameters(IndexedContainer container, Table table) {
		ProductoModel model = new ProductoModel();
		List<Producto> productos = model.findProductoByParameters(idSrch.getValue());
		container.removeAllItems();
		for(Producto producto : productos) {
			Object id = container.addItem();
			Item item = container.getItem(id);
			item.getItemProperty(UIConstants.PRODUCTO_NOMBRE).setValue(producto.getNombre());
			item.getItemProperty(UIConstants.PRODUCTO_PRECIO_COMPRA).setValue(producto.getPrecioCompra());
			item.getItemProperty(UIConstants.PRODUCTO_PRECIO_VENTA).setValue(ProductoUtils.calculatePrecioVenta(producto.getPrecioCompra(), producto.getPorcentaje()));
			item.getItemProperty(UIConstants.PRODUCTO_IVA).setValue(producto.getIva()? "Si":"No");
			item.getItemProperty(UIConstants.LAUNCH_WINDOW).setValue(createLaunchButton((UI)table.getParent().getParent().getParent(), (Window)table.getParent().getParent(), producto.getId()));
			item.getItemProperty(UIConstants.REMOVE).setValue(createRemoveButton(table, id, producto.getId()));
		
		}
		
		table.refreshRowCache();
	}
	
}
