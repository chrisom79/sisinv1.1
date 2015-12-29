package com.chrisom.sisinv.dao;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SessionFactoryDB {
	private static SessionFactory sessionFactory;
    private static StandardServiceRegistryBuilder serviceRegistry;
    
    static
    {
        try
        {
        	Configuration configuration = new Configuration().configure();

            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(serviceRegistry.build());
        }
        catch (HibernateException he)
        {
            System.err.println("Error creando Session: " + he);
            throw new ExceptionInInitializerError(he);
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    } 
    
    public static void shutdown() {
        getSessionFactory().close();
    }
}
