package com.chrisom.sisinv.utils;

import com.chrisom.sisinv.ui.UIConstants;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;

public class ProductoUtils {
	public static Double calculatePrecioVenta(Double precioCompra, Integer ganancia){
		Double total = precioCompra + (precioCompra * (ganancia.doubleValue() / 100.0));
		
		return total;
	}
	
	public static Double sumaImportes(Table table){
		Double total = new Double(0);
		table.getItemIds();
		for(Object id : table.getItemIds()) {
			total += (Double) table.getItem(id).getItemProperty(UIConstants.PEDIDO_IMPORTE).getValue();
		}
		
		return round(total, 2);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
