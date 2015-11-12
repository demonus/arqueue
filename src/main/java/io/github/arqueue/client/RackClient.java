package io.github.arqueue.client;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.api.jcloud.InstanceGenerator;
import io.github.arqueue.common.Utils;
import io.github.arqueue.core.runners.TaskData;
import io.github.arqueue.exception.AuthenticationException;
import io.github.arqueue.exception.CacheException;
import io.github.arqueue.exception.OpenStackApiException;
import io.github.arqueue.exception.ValidationException;
import io.github.arqueue.hibernate.SessionFactory;
import io.github.arqueue.hibernate.beans.Task;
import io.github.arqueue.hibernate.beans.User;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;

import java.util.Set;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args)
			throws AuthenticationException, InterruptedException, CacheException, OpenStackApiException
	{
		PropertyConfigurator.configure("./conf/log4j.properties");

		RackspaceConnect connect = new RackspaceConnect();

		connect.login("longjump", args[0]);

		System.out.println(connect.getServerDetails());

		System.out.println(connect.listFlavors());
	}
}
