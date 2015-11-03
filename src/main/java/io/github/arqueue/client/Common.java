package io.github.arqueue.client;

import io.github.arqueue.hibernate.SessionFactory;
import io.github.arqueue.hibernate.beans.Action;
import io.github.arqueue.hibernate.beans.Flow;
import io.github.arqueue.hibernate.beans.Task;
import io.github.arqueue.hibernate.beans.User;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;

/**
 * Created by remote on 10/29/15.
 */
public class Common
{
	public static void main(String[] args)
	{
		String json = "{\"id\":\"AHK232a23kj232UJjh454ee\",\"type\":\"server\",\"action\":\"CREATE\"," +
				"\"server\":{\"hardware\":\"4\",\"image\":\"44ec8fd7-89fe-4cff-8655-b06be28f98c1\"}}";

		PropertyConfigurator.configure("./conf/log4j.properties");

		SessionFactory sessionFactory = SessionFactory.getInstance();

		Session session = sessionFactory.openSession();

		try
		{
			session.beginTransaction();

			User user = session.get(User.class, "ff80818150b70f630150b70f65c70000");


			Flow flow = new Flow();

			flow.setName("Create a server");

			flow.setStatus(Flow.Status.NEW);

			flow.setUser(user);

			flow.setForceNoCache(false);

			user.getFlows().add(flow);

			session.save(flow);


			Action action = new Action();

			action.setParallelMax(1);

			action.setFlow(flow);

			action.setStatus(Flow.Status.NEW);

			flow.getActions().add(action);

			session.save(action);


			Task task = new Task();

			task.setStatus(Flow.Status.NEW);

			task.setData(json);

			task.setAction(action);

			action.getTasks().add(task);

			session.save(task);

			session.getTransaction().commit();

		}
		finally
		{
			session.close();

			sessionFactory.close();
		}
	}
}
