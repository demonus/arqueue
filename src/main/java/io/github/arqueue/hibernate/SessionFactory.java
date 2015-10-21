package io.github.arqueue.hibernate;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.StatelessSession;
import org.hibernate.StatelessSessionBuilder;
import org.hibernate.TypeHelper;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

/**
 * Created by root on 10/19/15.
 */
public class SessionFactory
{
	private org.hibernate.SessionFactory sessionFactory;

	private static volatile SessionFactory instance;

	public static SessionFactory getInstance()
	{
		if (instance == null)
		{
			synchronized (SessionFactory.class)
			{
				if (instance == null)
				{
					instance = new SessionFactory();
				}
			}
		}

		return instance;
	}

	private SessionFactory()
	{
		Configuration configuration = new Configuration();

		configuration.setProperties(io.github.arqueue.common.Configuration.getInstance().getSubset("hibernate."));

		configuration.addAnnotatedClass(io.github.arqueue.hibernate.beans.Flow.class);
		configuration.addAnnotatedClass(io.github.arqueue.hibernate.beans.Action.class);
		configuration.addAnnotatedClass(io.github.arqueue.hibernate.beans.Task.class);

		StandardServiceRegistryBuilder serviceRegistryBuilder =
				new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());

		sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.build());
	}

	public SessionFactoryOptions getSessionFactoryOptions()
	{
		return sessionFactory.getSessionFactoryOptions();
	}

	public Map<String, ClassMetadata> getAllClassMetadata()
	{
		return sessionFactory.getAllClassMetadata();
	}

	public StatelessSession openStatelessSession(Connection connection)
	{
		return sessionFactory.openStatelessSession(connection);
	}

	public ClassMetadata getClassMetadata(Class entityClass)
	{
		return sessionFactory.getClassMetadata(entityClass);
	}

	public void close() throws HibernateException
	{
		sessionFactory.close();
	}

	public Cache getCache()
	{
		return sessionFactory.getCache();
	}

	public StatelessSessionBuilder withStatelessOptions()
	{
		return sessionFactory.withStatelessOptions();
	}

	public SessionBuilder withOptions()
	{
		return sessionFactory.withOptions();
	}

	public ClassMetadata getClassMetadata(String entityName)
	{
		return sessionFactory.getClassMetadata(entityName);
	}

	public boolean containsFetchProfileDefinition(String name)
	{
		return sessionFactory.containsFetchProfileDefinition(name);
	}

	public Map getAllCollectionMetadata()
	{
		return sessionFactory.getAllCollectionMetadata();
	}

	public Set getDefinedFilterNames()
	{
		return sessionFactory.getDefinedFilterNames();
	}

	public StatelessSession openStatelessSession()
	{
		return sessionFactory.openStatelessSession();
	}

	public boolean isClosed()
	{
		return sessionFactory.isClosed();
	}

	public Session openSession() throws HibernateException
	{
		return sessionFactory.openSession();
	}

	public CollectionMetadata getCollectionMetadata(String roleName)
	{
		return sessionFactory.getCollectionMetadata(roleName);
	}

	public Statistics getStatistics()
	{
		return sessionFactory.getStatistics();
	}

	public FilterDefinition getFilterDefinition(String filterName) throws HibernateException
	{
		return sessionFactory.getFilterDefinition(filterName);
	}

	public Reference getReference() throws NamingException
	{
		return sessionFactory.getReference();
	}

	public Session getCurrentSession() throws HibernateException
	{
		return sessionFactory.getCurrentSession();
	}

	public TypeHelper getTypeHelper()
	{
		return sessionFactory.getTypeHelper();
	}
}
