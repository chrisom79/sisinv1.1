package com.chrisom.sisinv.ui;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.chrisom.report.ReportGenerator;
import com.chrisom.sisinv.entity.NotaRemision;
import com.chrisom.sisinv.entity.NotaRemisionDetalle;
import com.chrisom.sisinv.entity.NotaRemisionDetalleId;
import com.chrisom.sisinv.entity.Producto;
import com.chrisom.sisinv.entity.Vendedor;
import com.chrisom.sisinv.model.PedidoModel;
import com.chrisom.sisinv.model.ProductoModel;
import com.chrisom.sisinv.model.VendedorModel;
import com.chrisom.sisinv.utils.ProductoUtils;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class PedidoUI {
	DateField fecha = null;
	TextField numero = null;
	TextField nombre = null;
	TextField prodSearcher = null;
	TextField vendSearcher = null;
	Set<NotaRemisionDetalle> prodsPedido = null;
	Vendedor vendPedido = null;
	Panel addProdPnl = null;
	Panel mngmtProdPnl = null;
	Panel vendProdPnl = null;
	IndexedContainer pedidoCntnr = null;
	Table pedidoTbl = null;
	PedidoModel model = new PedidoModel();
	Label totalLbl = null;
	NotaRemision nr = null;
	
	public Window createWindowNew() {
		Window notaWndw = new Window(UIConstants.PEDIDO_ITEM);
		VerticalLayout content = new VerticalLayout();
		GridLayout subcontent1 = new GridLayout(5, 5);
		HorizontalLayout hcontent = new HorizontalLayout();
		
		prodsPedido = new HashSet<NotaRemisionDetalle>();
		
		
		fecha = new DateField(UIConstants.PEDIDO_FECHA);
		numero = new TextField(UIConstants.PEDIDO_NUMERO);
		nombre = new TextField(UIConstants.PEDIDO_NOMBRE);
		addProdPnl = createPanelAddProductos();
		mngmtProdPnl = createPanelMngmtProductos();
		vendProdPnl = createPanelVendedorProductos();
		Button cartButton = createCartButton();
		Button pedidoBtn = createPedidoButton();
		Button vendBtn = createVendedorButton();
		
		fecha.setValue(new Date());
		numero.setValue(model.getNewId().toString());
		nombre.setWidth("400");
		hcontent.addComponent(pedidoBtn);
		hcontent.addComponent(vendBtn);
		hcontent.addComponent(cartButton);
		
		subcontent1.addComponent(fecha, 0, 0);
		subcontent1.addComponent(numero, 1, 0);
		subcontent1.addComponent(nombre, 2, 0, 4, 0);
		subcontent1.addComponent(hcontent, 0, 1);
		subcontent1.addComponent(addProdPnl, 0, 2, 4, 3);
		hcontent.setMargin(true);
		hcontent.setSpacing(true);
		subcontent1.setMargin(true);
		subcontent1.setSpacing(true);
		prodSearcher.focus();
		
		content.addComponent(subcontent1);
		
		notaWndw.setContent(content);
		notaWndw.center();
		
		return notaWndw;
	}
	
	private TextField createSearcher(IndexedContainer container, Table result) {
		TextField prodSrchr = new TextField(UIConstants.PEDIDO_PRODUCTO_SRCHR);
		
		
		prodSrchr.addShortcutListener(new ShortcutListener("shortcutId", ShortcutAction.KeyCode.ENTER, null) {
		    @Override
		    public void handleAction(Object sender, Object target) {
		    	executeFindProductosByParameters(container, result);
		    }
		});
		
		return prodSrchr;
	}
	
	private TextField createVendedorSearcher(IndexedContainer container, Table result) {
		TextField vendSrchr = new TextField(UIConstants.PEDIDO_VENDEDOR_SRCHR);
		
		
		vendSrchr.addShortcutListener(new ShortcutListener("shortcutId", ShortcutAction.KeyCode.ENTER, null) {
		    @Override
		    public void handleAction(Object sender, Object target) {
		    	executeFindVendedorByParameters(container, result);
		    }
		});
		
		return vendSrchr;
	}
	
	private Button createAddButton(Producto prod, TextField cant) {
		Button button = new Button();
		button.setIcon(FontAwesome.PLUS_SQUARE_O);
		button.setId(prod.getId());
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(cant.getValue() != null && !cant.getValue().isEmpty()) {
					NotaRemisionDetalle item = new NotaRemisionDetalle();
					item.setId(Integer.valueOf(numero.getValue()));
					item.setProductoId(prod.getId());
					if(cant.getValue() != null && !cant.getValue().isEmpty())
						item.setCantidad(Integer.valueOf(cant.getValue()));
					item.setPrecio(ProductoUtils.calculatePrecioVenta(prod.getPrecioCompra(), prod.getPorcentaje()));
					prodsPedido.add(item);
					
					refreshPedidoContainer();
					totalLbl.setValue("Total: " + ProductoUtils.sumaImportes(pedidoTbl));
					Notification.show("Producto agregado",
			                  "El producto ha sido agregado al pedido",
			                  Notification.Type.TRAY_NOTIFICATION);
				} else {
					Notification.show("Favor de asignar una cantidad de producto",
			                  Notification.Type.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}
	
	private Button createAddVendButton(Vendedor vend) {
		Button button = new Button();
		button.setIcon(FontAwesome.PLUS_SQUARE_O);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				vendPedido = vend;
				Notification.show("Vendedor agregado",
						"El vendedor ha sido agregado al pedido",
			            Notification.Type.TRAY_NOTIFICATION);
			}
		});

		return button;
	}
	
	private Button createCartButton() {
		Button button = new Button();
		button.setIcon(FontAwesome.SHOPPING_CART);

		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				GridLayout layout = (GridLayout) (addProdPnl.getParent() != null ? addProdPnl.getParent() : vendProdPnl.getParent());
				if(addProdPnl.isVisible() || vendProdPnl.isVisible()) {
					addProdPnl.setVisible(false);
					vendProdPnl.setVisible(false);
					mngmtProdPnl.setVisible(true);
					layout.removeComponent(addProdPnl);
					layout.removeComponent(vendProdPnl);
					layout.addComponent(mngmtProdPnl, 0, 2, 4, 3);
				}
			}
		});

		return button;
	}
	
	private Button createVendedorButton() {
		Button button = new Button();
		button.setIcon(FontAwesome.USER);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				GridLayout layout = (GridLayout) (addProdPnl.getParent()!= null ? addProdPnl.getParent() : mngmtProdPnl.getParent());
				if(addProdPnl.isVisible() || mngmtProdPnl.isVisible()) {
					addProdPnl.setVisible(false);
					mngmtProdPnl.setVisible(false);
					vendProdPnl.setVisible(true);
					layout.removeComponent(addProdPnl);
					layout.removeComponent(mngmtProdPnl);
					layout.addComponent(vendProdPnl, 0, 2, 4, 3);
				}
			}
		});

		return button;
	}
	
	private Button createPedidoButton() {
		Button button = new Button();
		button.setIcon(FontAwesome.TRUCK);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				GridLayout layout = (GridLayout) (mngmtProdPnl.getParent()!= null ? mngmtProdPnl.getParent() : vendProdPnl.getParent());
				if(mngmtProdPnl.isVisible() || vendProdPnl.isVisible()) {
					mngmtProdPnl.setVisible(false);
					vendProdPnl.setVisible(false);
					addProdPnl.setVisible(true);
					layout.removeComponent(mngmtProdPnl);
					layout.removeComponent(vendProdPnl);
					layout.addComponent(addProdPnl, 0, 2, 4, 3);
				}
			}
		});

		return button;
	}
	
	private Button createBorrarButton(Table table, Object id) {
		Button button = new Button();
		button.setIcon(FontAwesome.TRASH_O);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				table.removeItem(id);
				table.refreshRowCache();
			}
		});

		return button;
	}
	
	private Button createGuardarButton() {
		Button button = new Button(UIConstants.GUARDAR);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				createPedidoObject();
				String id = model.insertPedido(nr);
				if(id != null || !id.isEmpty()) {
					Notification.show("Pedido guardado",
							"El pedido ha sido guardado",
				            Notification.Type.TRAY_NOTIFICATION);
				}
			}
		});

		return button;
	}
	
	private Button createImprimirButton() {
		Button button = new Button(UIConstants.IMPRIMIR);
		
		button.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ReportGenerator rg = new ReportGenerator();
				
				createPedidoObject();
				rg.callingPedido(nr);
			}
		});

		return button;
	}
	
	private TextField createTxtCantidad(String id) {
		TextField txtCantidad = new TextField();
		txtCantidad.setId("txt" + id);
		txtCantidad.setWidth("50");
		
		return txtCantidad;
	}
	
	private Panel createPanelAddProductos() {
		Panel prodPnl = new Panel();
		VerticalLayout layout =  new VerticalLayout();
		Table result = new Table();
		IndexedContainer container = createProductosContainer();
		
		prodSearcher = createSearcher(container, result);
		
		prodSearcher.setWidth("400");
		result.setContainerDataSource(container);
		result.setSelectable(true);
		result.setPageLength(5);
		result.setWidth("700");
		
		layout.addComponent(prodSearcher);
		layout.addComponent(result);
		
		layout.setSpacing(true);
		layout.setMargin(true);
		
		prodPnl.setContent(layout);
		return prodPnl;
	}
	
	private Panel createPanelMngmtProductos() {
		Panel prodPnl = new Panel();
		VerticalLayout layout =  new VerticalLayout();
		HorizontalLayout btnsArea = new HorizontalLayout();
		totalLbl = new Label();
		pedidoTbl = new Table();
		pedidoCntnr = createPedidoContainer();
		Button guardarBtn = createGuardarButton();
		Button imprimirBtn = createImprimirButton();
		Label lblPedido = new Label(UIConstants.PEDIDO_CREANDO);
		
		pedidoTbl.setContainerDataSource(pedidoCntnr);
		pedidoTbl.setSelectable(true);
		pedidoTbl.setPageLength(5);
		pedidoTbl.setWidth("700");
		
		btnsArea.addComponent(guardarBtn);
		btnsArea.addComponent(imprimirBtn);
		btnsArea.setSpacing(true);
		
		layout.addComponent(lblPedido);
		layout.addComponent(pedidoTbl);
		layout.addComponent(totalLbl);
		layout.setComponentAlignment(totalLbl, Alignment.MIDDLE_LEFT);
		layout.addComponent(btnsArea);
		
		layout.setSpacing(true);
		layout.setMargin(true);
		
		prodPnl.setContent(layout);
		return prodPnl;
	}
	
	private Panel createPanelVendedorProductos() {
		Panel vendedorPnl = new Panel();
		VerticalLayout layout =  new VerticalLayout();
		Table result = new Table();
		IndexedContainer container = createVendedorContainer();
		//Label lblVendedor = new Label(UIConstants.PEDIDO_CREANDO);
		
		vendSearcher = createVendedorSearcher(container, result);
		
		vendSearcher.setWidth("400");
		result.setContainerDataSource(container);
		result.setSelectable(true);
		result.setPageLength(5);
		result.setWidth("700");
		
		layout.addComponent(vendSearcher);
		layout.addComponent(result);
		
		layout.setSpacing(true);
		layout.setMargin(true);
		
		vendedorPnl.setContent(layout);
		return vendedorPnl;
	}
	
	private IndexedContainer createProductosContainer() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(UIConstants.PRODUCTO_NOMBRE, String.class, null);
		container.addContainerProperty(UIConstants.PRODUCTO_PRECIO_VENTA, Double.class, null);
		container.addContainerProperty(UIConstants.PRODUCTO_IVA, String.class, null);
		container.addContainerProperty(UIConstants.PEDIDO_CANTIDAD, TextField.class, null);
		container.addContainerProperty(UIConstants.AGREGAR, Button.class, null);
		
		return container;
	}
	
	private IndexedContainer createPedidoContainer() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(UIConstants.PRODUCTO_NOMBRE, String.class, null);
		container.addContainerProperty(UIConstants.PRODUCTO_PRECIO_VENTA, Double.class, null);
		container.addContainerProperty(UIConstants.PRODUCTO_IVA, String.class, null);
		container.addContainerProperty(UIConstants.PEDIDO_CANTIDAD, Integer.class, null);
		container.addContainerProperty(UIConstants.PEDIDO_IMPORTE, Double.class, null);
		container.addContainerProperty(UIConstants.BUTTON_CANCELAR, Button.class, null);
		
		return container;
	}
	
	private IndexedContainer createVendedorContainer() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(UIConstants.VENDEDOR_NOMBRE, String.class, null);
		container.addContainerProperty(UIConstants.VENDEDOR_USUARIO, String.class, null);
		container.addContainerProperty(UIConstants.AGREGAR, Button.class, null);
		
		return container;
	}
	@SuppressWarnings("unchecked")
	private void executeFindProductosByParameters(IndexedContainer container, Table table) {
		ProductoModel model = new ProductoModel();
		List<Producto> productos = model.findProductoByParameters(prodSearcher.getValue());
		container.removeAllItems();
		for(Producto producto : productos) {
			Object id = container.addItem();
			Item item = container.getItem(id);
			TextField txCantidad = createTxtCantidad(producto.getId());
			item.getItemProperty(UIConstants.PRODUCTO_NOMBRE).setValue(producto.getNombre());
			item.getItemProperty(UIConstants.PRODUCTO_PRECIO_VENTA).setValue(ProductoUtils.calculatePrecioVenta(producto.getPrecioCompra(), producto.getPorcentaje()));
			item.getItemProperty(UIConstants.PRODUCTO_IVA).setValue(producto.getIva()? "Si":"No");
			item.getItemProperty(UIConstants.PEDIDO_CANTIDAD).setValue(txCantidad);
			item.getItemProperty(UIConstants.AGREGAR).setValue(createAddButton(producto, txCantidad));
		}
		
		table.refreshRowCache();
	}
	
	private void executeFindVendedorByParameters(IndexedContainer container, Table table) {
		VendedorModel model = new VendedorModel();
		List<Vendedor> vendedores = model.findVendedoresByParameters(vendSearcher.getValue());
		container.removeAllItems();
		for(Vendedor vendedor : vendedores) {
			Object id = container.addItem();
			Item item = container.getItem(id);
			
			item.getItemProperty(UIConstants.VENDEDOR_NOMBRE).setValue(vendedor.getNombre());
			item.getItemProperty(UIConstants.VENDEDOR_USUARIO).setValue(vendedor.getUsuario());
			item.getItemProperty(UIConstants.AGREGAR).setValue(createAddVendButton(vendedor));
		}
		
		table.refreshRowCache();
	}
	
	@SuppressWarnings("unchecked")
	private void refreshPedidoContainer() {
		pedidoCntnr.removeAllItems();
		ProductoModel model = new ProductoModel();
		for(NotaRemisionDetalle detalle : prodsPedido) {
			Object id = pedidoCntnr.addItem();
			Item item = pedidoCntnr.getItem(id);
			Producto producto = model.findProductoByCode(detalle.getProductoId());
			Double precioVenta = ProductoUtils.calculatePrecioVenta(producto.getPrecioCompra(), producto.getPorcentaje());
			Double importe = precioVenta * detalle.getCantidad();
			item.getItemProperty(UIConstants.PRODUCTO_NOMBRE).setValue(producto.getNombre());
			item.getItemProperty(UIConstants.PRODUCTO_PRECIO_VENTA).setValue(precioVenta);
			item.getItemProperty(UIConstants.PRODUCTO_IVA).setValue(producto.getIva()? "Si":"No");
			item.getItemProperty(UIConstants.PEDIDO_CANTIDAD).setValue(detalle.getCantidad());
			item.getItemProperty(UIConstants.PEDIDO_IMPORTE).setValue(importe);
			item.getItemProperty(UIConstants.BUTTON_CANCELAR).setValue(createBorrarButton(pedidoTbl, id));
		}
		pedidoTbl.refreshRowCache();
	}
	
	private void createPedidoObject() {
		if(nr == null) {
			nr = new NotaRemision();
			
			nr.setId(Integer.parseInt(numero.getValue()));
			nr.setTotal(ProductoUtils.sumaImportes(pedidoTbl));
			nr.setFecha(fecha.getValue());
			nr.setVendedor(vendPedido);
			nr.setNombre(nombre.getValue());
			for(NotaRemisionDetalle nrd : prodsPedido) {
				nrd.setNotaRemision(nr);
				nr.getNotaRemisionDetalles().add(nrd);
			}
		}
	}
}
