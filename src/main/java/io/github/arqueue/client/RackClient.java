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

/*		SessionFactory sessionFactory = SessionFactory.getInstance();

		Session session = sessionFactory.openSession();

		try
		{

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

			Action action2 = new Action();

			action2.setFlow(flow);

			action2.setParallelMax(20);

			flow.getActions().add(action2);

			session.save(action2);

			Task task2 = new Task();

			task2.setAction(action);

			task2.setData("{ \"id\": \"aaaaaaaaaaa\"}");

			task2.setStatus(Flow.Status.IN_PROGRESS);

			action.getTasks().add(task2);

			session.save(task2);

			session.getTransaction().commit();

			session = sessionFactory.openSession();

			Flow fl = session.get(Flow.class, "ff80818150834fea0150834fedf70000");

			for (Action a : fl.getActions())
			{
				System.out.println(a.getOrderNumber() + " : " + a.getId());
			}

			session.close();
		}
		finally
		{

			sessionFactory.close();
		}

		System.out.println("Session is closed");*/

		long time1 = System.currentTimeMillis();

		InstanceGenerator instanceGenerator = new InstanceGenerator("longjump", args[0]);

		System.out.println(System.currentTimeMillis() - time1);

		time1 = System.currentTimeMillis();

		NovaApi novaApi = instanceGenerator.getNovaApiInstance();

		System.out.println(System.currentTimeMillis() - time1);

		Thread.currentThread().sleep(4000);

		time1 = System.currentTimeMillis();

		NovaApi novaApi2 = instanceGenerator.getNovaApiInstance();

		System.out.println(System.currentTimeMillis() - time1);

		Thread.currentThread().sleep(4000);

		time1 = System.currentTimeMillis();

		ComputeService computeService = instanceGenerator.getComputeService();

		System.out.println(System.currentTimeMillis() - time1);

		time1 = System.currentTimeMillis();

		/*ComputeService computeService = instanceGenerator.getComputeService();

		Set<? extends Image> images = computeService.listImages();


		for (Image image : images)
		{
			if ("snapshot".equals(image.getUserMetadata().get("image_type")))
			{
				System.out.println(image.getName() + " : " + image.getUserMetadata().get("image_type") + " : " +
						image.getProviderId() + " : " + image.getTags());
			}
		}*/
	}
}
