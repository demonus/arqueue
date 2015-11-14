package io.github.arqueue.client;

import io.github.arqueue.api.OpenStackConnection;
import io.github.arqueue.api.beans.get.image.Image;
import io.github.arqueue.api.beans.get.image.Images;
import io.github.arqueue.common.Utils;
import io.github.arqueue.core.runners.TaskData;
import io.github.arqueue.exception.AuthenticationException;
import io.github.arqueue.exception.CacheException;
import io.github.arqueue.exception.OpenStackApiException;
import io.github.arqueue.exception.ValidationException;
import io.github.arqueue.hibernate.SessionFactory;
import io.github.arqueue.hibernate.beans.Task;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args)
			throws AuthenticationException, InterruptedException, CacheException, OpenStackApiException,
			ValidationException
	{
		PropertyConfigurator.configure("./conf/log4j.properties");

		OpenStackConnection connect = new OpenStackConnection();

		connect.login("longjump", args[0]);

		Images images = connect.listSnapshotImages();

		for (Image image : images.getImages())
		{
			System.out.println(image.getName() + " | " + image.getId());
		}


		/*SessionFactory sessionFactory = SessionFactory.getInstance();

		Session session = sessionFactory.openSession();

		try
		{
			Task task = session.get(Task.class, "ff80818150b72f8c0150b72f8fe50002");

			TaskData taskData = TaskData.parse(task);

			taskData.getNode().build(connect);
		}
		finally
		{
			Utils.closeResources(session, sessionFactory);
		}*/
	}
}
