package com.chrisom.sisinv.model;

import java.util.List;

import com.chrisom.sisinv.dao.VendedorDAO;
import com.chrisom.sisinv.entity.Vendedor;
import com.chrisom.sisinv.utils.Algorithms;

public class VendedorModel {
	VendedorDAO dao = new VendedorDAO();
	
	public void insertVendedor(Vendedor vendedor) {
		dao.insert(vendedor);		
	}
	
	public void updateVendedor() {
		
	}
	
	public String createId(String nombre) {
		String[] splitted = nombre.trim().split("\\s");
		StringBuffer sb = new StringBuffer();
		
		for(String name : splitted){
			if(name != null && !name.trim().isEmpty())
				sb.append(name.substring(0, 1));
		}
		sb.append(dao.countIds(sb.toString()) + 1);
		return sb.toString().toLowerCase();
	}
	
	public boolean isLoginSuccess(String user, String password) {
		if(dao.findVendedorByUserAndPassword(user, Algorithms.encryptMD5(password)) != null) {
			return true;
		}
		
		return false;
	}
	
	public boolean existsUsername(String username) {
		if(dao.countUsernames(username) > 0) {
			return true;
		}
		
		return false;
	}
	
	public List<Vendedor> findVendedoresByParameters(String nombre, String telefono, String usuario) {
		return dao.findVendedorByParameters(nombre, telefono, usuario);
	}
	
	public List<Vendedor> findVendedoresByParameters(String value) {
		return dao.findVendedorByUnifiedParameters(value);
	}
	
	public Vendedor findVendedorByUsuario(String username){
		return dao.findVendedorByUsuario(username);
	}
	
	public void deleteByUsuario(String usuario){
		dao.deleteByField(usuario);
	}
}
