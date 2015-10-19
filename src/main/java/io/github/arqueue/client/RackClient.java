package io.github.arqueue.client;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.exception.AuthenticationException;
import io.github.arqueue.hibernate.SessionFactory;
import io.github.arqueue.hibernate.beans.Action;
import io.github.arqueue.hibernate.beans.Flow;
import io.github.arqueue.hibernate.beans.Task;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args) throws AuthenticationException
	{
		PropertyConfigurator.configure("./conf/log4j.properties");

		RackspaceConnect rackConnect = new RackspaceConnect();

		SessionFactory sessionFactory = SessionFactory.getInstance();

		Session session = sessionFactory.openSession();

		System.out.println("Session is open");

		session.beginTransaction();

		Flow flow = new Flow();

		flow.setName("Test flow");
		flow.setStatus(Flow.Status.PENDING);
		flow.setToken("134567890");

		session.save(flow);

		Action action = new Action();

		action.setFlow(flow);

		action.setParallelMax(3);

		flow.getActions().add(action);

		session.save(action);

		Task task = new Task();

		task.setAction(action);

		task.setData("{ \"id\": \"2345678\"}");

		task.setStatus(Flow.Status.NEW);

		action.getTasks().add(task);

		session.save(task);

		Task task2 = new Task();

		task2.setAction(action);

		task2.setData("{ \"id\": \"aaaaaaaaaaa\"}");

		task2.setStatus(Flow.Status.IN_PROGRESS);

		action.getTasks().add(task2);

		session.save(task2);

		session.getTransaction().commit();

		System.out.println("Session is closed");

		/*rackConnect.login("longjump", args[0]);

		ServerDetailsArray serverDetailsArray = rackConnect.getServerDetails();

		for (ServerDetails serverDetails : serverDetailsArray.getServers())
		{
			System.out.println(serverDetails.getId() + " " + serverDetails.getName());
		}*/
	}
}
