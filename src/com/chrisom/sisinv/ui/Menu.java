package com.chrisom.sisinv.ui;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

public class Menu {
	public MenuBar init(UI ui) {
		MenuBar mainBar =  new MenuBar();
		VendedorUI vendedorUI = new VendedorUI();
		ProductoUI productoUI = new ProductoUI();
		PedidoUI notaUI = new PedidoUI();
		
		MenuBar.Command command = new MenuBar.Command() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -5343790692386576486L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				if(selectedItem.getText().equals(UIConstants.CREAR_USUARIO_ITEM)) {
					ui.addWindow(vendedorUI.createWindow());;
				} else if(selectedItem.getText().equals(UIConstants.BUSCAR_USUARIO_ITEM)){
					ui.addWindow(vendedorUI.createWindowSrch());
				} else if(selectedItem.getText().equals(UIConstants.CREAR_PRODUCTO_ITEM)){
					ui.addWindow(productoUI.createWindowNew());
				} else if(selectedItem.getText().equals(UIConstants.BUSCAR_PRODUCTO_ITEM)){
					ui.addWindow(productoUI.createWindowSrch());
				} else if(selectedItem.getText().equals(UIConstants.CREAR_PEDIDO_ITEM)){
					ui.addWindow(notaUI.createWindowNew());
				}
			}
		};
		
		MenuItem usuarios = mainBar.addItem(UIConstants.USUARIO_ITEM, null, null);
		usuarios.addItem(UIConstants.CREAR_USUARIO_ITEM, command);
		usuarios.addItem(UIConstants.BUSCAR_USUARIO_ITEM, command);
		
		MenuItem productos = mainBar.addItem(UIConstants.PRODUCTO_ITEM, null, null);
		productos.addItem(UIConstants.CREAR_PRODUCTO_ITEM, command);
		productos.addItem(UIConstants.BUSCAR_PRODUCTO_ITEM, command);
		
		MenuItem notas = mainBar.addItem(UIConstants.PEDIDO_ITEM, null, null);
		notas.addItem(UIConstants.CREAR_PEDIDO_ITEM, command);
		
		return mainBar;
	}
}
