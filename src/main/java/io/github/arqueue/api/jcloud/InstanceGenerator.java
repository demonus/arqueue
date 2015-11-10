package io.github.arqueue.api.jcloud;

import com.google.common.io.Closeables;
import io.github.arqueue.common.Utils;
import io.github.arqueue.hibernate.beans.User;
import org.apache.log4j.Logger;
import org.jclouds.ContextBuilder;
import org.jclouds.View;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.openstack.nova.v2_0.NovaApi;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by root on 10/23/15.
 */
public class InstanceGenerator
{
	private Logger logger = Logger.getLogger(this.getClass());

	private User user = null;

	private String provider;

	private volatile ComputeService computeService = null;

	private volatile NovaApi novaApi = null;

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public InstanceGenerator(User user)
	{
		this.user = user;

		this.provider = System.getProperty("provider.cs", "rackspace-cloudservers-us");
	}

	public <T extends Closeable> T getApiInstance(Class<T> clazz)
	{
		System.out.println("0");
		ContextBuilder contextBuilder = ContextBuilder.newBuilder(provider);
		System.out.println("1");
		contextBuilder.credentials(user.getUsername(), user.getApiKey());
		System.out.println("3");

		return contextBuilder.buildApi(clazz);
	}

	public <T extends View> T getViewInstance(Class<T> clazz)
	{
		return ContextBuilder.newBuilder(provider).credentials(user.getUsername(), user.getApiKey()).buildView(clazz);
	}

	public NovaApi getNovaApiInstance()
	{
		if (novaApi == null)
		{
			synchronized (this)
			{
				if (novaApi == null)
				{
					novaApi = getApiInstance(NovaApi.class);
				}
			}
		}

		return novaApi;
	}

	public ComputeService getComputeService()
	{
		if (computeService == null)
		{
			synchronized (this)
			{
				if (computeService == null)
				{
					ComputeServiceContext computeServiceContext = getViewInstance(ComputeServiceContext.class);

					computeService = computeServiceContext.getComputeService();
				}
			}
		}

		return computeService;
	}

	public void close(Closeable... closeables)
	{
		for (Closeable closeable : closeables)
		{
			if (closeable != null)
			{
				try
				{
					Closeables.close(closeable, true);
				}
				catch (IOException ioeEx)
				{
					logger.error("Could not close " + (closeable.getClass().getName()) + ": " +
							Utils.getExceptionStackTrace(ioeEx));
				}
			}
		}
	}
}
