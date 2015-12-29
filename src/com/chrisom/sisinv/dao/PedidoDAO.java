package com.chrisom.sisinv.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.chrisom.sisinv.entity.NotaRemision;

public class PedidoDAO implements DAOInterface<NotaRemision> {

	@Override
	public String insert(NotaRemision element) {
		Session session = SessionFactoryDB.getSessionFactory().openSession();
		String id =  null;
		try {
			
			session.beginTransaction();
			id = session.save(element).toString();
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} 
		
		return id;
		
	}

	@Override
	public void update(NotaRemision element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long countIds(String id) {
		Session session = SessionFactoryDB.getSessionFactory().openSession();
		Long total = null;
		try {
			Query query = session.createQuery("select count(id) from NotaRemision");
		
			total = (Long) query.uniqueResult();
		} catch (Exception ex) {
			System.out.println(ex.getStackTrace());
		}
		
		return total;
	}

	@Override
	public void deleteByField(String field) {
		// TODO Auto-generated method stub
		
	}

}
