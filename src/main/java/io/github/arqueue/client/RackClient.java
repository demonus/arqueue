package io.github.arqueue.client;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.api.beans.ServerDetails;
import io.github.arqueue.api.beans.ServerDetailsArray;
import io.github.arqueue.api.jcloud.InstanceGenerator;
import io.github.arqueue.exception.AuthenticationException;
import io.github.arqueue.hibernate.SessionFactory;
import io.github.arqueue.hibernate.beans.Action;
import io.github.arqueue.hibernate.beans.Flow;
import io.github.arqueue.hibernate.beans.Task;
import io.github.arqueue.hibernate.beans.User;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Image;
import org.jclouds.openstack.nova.v2_0.NovaApi;

import java.util.Set;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args) throws AuthenticationException, InterruptedException
	{
		PropertyConfigurator.configure("./conf/log4j.properties");

		RackspaceConnect rackConnect = new RackspaceConnect();

		User user = new User();

		SessionFactory sessionFactory = SessionFactory.getInstance();

		Session session = sessionFactory.openSession();

		try
		{
			session.beginTransaction();

			user.setUsername("arqueue");
			user.setApiKey(args[0]);

			session.save(user);

			session.getTransaction().commit();

			User user2 = session.load(User.class, "ff80818150a63c750150a63c78810000");

			System.out.println(user2.getApiKey());
		}
		finally
		{
			session.close();

			sessionFactory.close();
		}
	}
}
