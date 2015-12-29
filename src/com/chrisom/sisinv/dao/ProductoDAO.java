package com.chrisom.sisinv.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.chrisom.sisinv.entity.Producto;

public class ProductoDAO implements DAOInterface<Producto> {

	@Override
	public String insert(Producto element) {
		Session session = SessionFactoryDB.getSessionFactory().openSession();
		String id = null;
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
	public void update(Producto element) {
		Session session = SessionFactoryDB.getSessionFactory().openSession();
		try {
			
			session.beginTransaction();
			session.saveOrUpdate(element);
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			//SessionFactoryDB.shutdown();
		}
		
	}

	@Override
	public Long countIds(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByField(String field) {
		Session session = SessionFactoryDB.getSessionFactory().openSession();
		try {
			
			session.beginTransaction();
			Query query = session.createQuery("delete Producto where id = :id");
			query.setParameter("id", field);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception ex) {
			session.getTransaction().rollback();
		} finally {
			//SessionFactoryDB.shutdown();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Producto> findProductoByNombre(String nombre){
		List<Producto> productos = new ArrayList<Producto>();
		Session session = SessionFactoryDB.getSessionFactory().openSession();
		Query query = session.createQuery("from Producto where nombre like :nombre");
		
		query.setParameter("nombre", "%" + nombre + "%");
		
		productos = query.list();
		return productos;
	}
	
	public Producto findProductoByCode(String id){
		Producto producto = new Producto();
		Session session = SessionFactoryDB.getSessionFactory().openSession();
		Query query = session.createQuery("from Producto where id like :id");
		
		query.setParameter("id", id);
		
		producto = (Producto) query.uniqueResult();
		return producto;
	}
	
	@SuppressWarnings("unchecked")
	public List<Producto> findProductoByParameters(String id) {
		List<Producto> productos = new ArrayList<Producto>();
		Session session = SessionFactoryDB.getSessionFactory().openSession();
		StringBuffer querySb = new StringBuffer("from Producto where id like :id or nombre like :id");
		
		Query query = session.createQuery(querySb.toString()); 
		
		if(id != null && !id.isEmpty()) {
			query.setParameter("id", "%" + id + "%");
		} 
		
		productos = query.list();
		return productos;
	}

}
