package com.chrisom.sisinv.model;

import java.util.List;

import com.chrisom.sisinv.dao.ProductoDAO;
import com.chrisom.sisinv.entity.Producto;

public class ProductoModel {
	ProductoDAO dao = new ProductoDAO();
	public void insertProducto(Producto producto) {
		dao.insert(producto);
	}
	
	public void updateProducto(Producto producto) {
		dao.update(producto);
	}
	
	public void deleteById(String id) {
		dao.deleteByField(id);
	}
	
	public List<Producto> findProductoByNombre(String nombre) {
		return dao.findProductoByNombre(nombre);
	}
	
	public List<Producto> findProductoByParameters(String id) {
		return dao.findProductoByParameters(id);
	}
	
	public Producto findProductoByCode(String id) {
		return dao.findProductoByCode(id);
	}
}
