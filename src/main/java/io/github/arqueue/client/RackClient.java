package io.github.arqueue.client;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.api.beans.ServerDetails;
import io.github.arqueue.api.beans.ServerDetailsArray;
import io.github.arqueue.api.jcloud.InstanceGenerator;
import io.github.arqueue.api.jcloud.JCloudUtils;
import io.github.arqueue.api.jcloud.StaticDataCache;
import io.github.arqueue.common.Utils;
import io.github.arqueue.core.runners.TaskData;
import io.github.arqueue.exception.AuthenticationException;
import io.github.arqueue.exception.CacheException;
import io.github.arqueue.exception.ValidationException;
import io.github.arqueue.hibernate.SessionFactory;
import io.github.arqueue.hibernate.beans.Action;
import io.github.arqueue.hibernate.beans.Flow;
import io.github.arqueue.hibernate.beans.Task;
import io.github.arqueue.hibernate.beans.User;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.openstack.nova.v2_0.NovaApi;

import java.util.Map;
import java.util.Set;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args) throws AuthenticationException, InterruptedException, CacheException
	{
		PropertyConfigurator.configure("./conf/log4j.properties");

		RackspaceConnect rackConnect = new RackspaceConnect();

		User user = new User();

		user.setUsername("longjump");
		user.setApiKey(args[0]);

		InstanceGenerator instanceGenerator = new InstanceGenerator(user);

		long time = System.currentTimeMillis();

		SessionFactory sessionFactory = SessionFactory.getInstance();

		Session session = sessionFactory.openSession();

		try
		{
			Task task = session.get(Task.class, "ff80818150b72f8c0150b72f8fe50002");

			TaskData taskData = TaskData.parse(task);

			Set<? extends NodeMetadata> result = taskData.getNode().build(instanceGenerator);

			for (NodeMetadata metadata : result)
			{
				System.out.println(metadata.getName());
				System.out.println(metadata.getCredentials().getOptionalPassword().get());
				System.out.println(metadata.getGroup());
				System.out.println(metadata.getHostname());
				System.out.println(metadata.getOperatingSystem().getName());
				System.out.println(metadata.getPublicAddresses());
				System.out.println(metadata.getPrivateAddresses());
				System.out.println(metadata.getStatus());
			}
		}
		catch (RunNodesException e)
		{
			e.printStackTrace();
		}
		catch (ValidationException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Utils.closeResources(session, sessionFactory);
		}

		System.out.println("******* EXECUTION TIME: " + ((System.currentTimeMillis() - time) / 1000) + " seconds");

	}
}
