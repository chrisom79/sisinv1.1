package com.chrisom.sisinv.model;

import com.chrisom.sisinv.dao.PedidoDAO;
import com.chrisom.sisinv.entity.NotaRemision;

public class PedidoModel {
	PedidoDAO dao = new PedidoDAO();
	
	public Long getNewId() {
		Long id = null;
		
		id = dao.countIds(null);
		if(id == null) {
			id = new Long(0);
		}
		
		return id + 1;
	}
	
	public String insertPedido(NotaRemision pedido) {
		return dao.insert(pedido);
	}
}
