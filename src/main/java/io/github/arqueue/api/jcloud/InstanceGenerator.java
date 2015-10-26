package io.github.arqueue.api.jcloud;

import com.google.common.io.Closeables;
import io.github.arqueue.common.Utils;
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

	private String username;

	private String apiKey;

	private String provider;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getApiKey()
	{
		return apiKey;
	}

	public void setApiKey(String apiKey)
	{
		this.apiKey = apiKey;
	}

	public InstanceGenerator(String username, String apiKey)
	{
		this.username = username;
		this.apiKey = apiKey;

		this.provider = System.getProperty("provider.cs", "rackspace-cloudservers-us");

		ContextBuilder.newBuilder(provider).credentials(username, apiKey).buildInjector();
	}

	public <T extends Closeable> T getApiInstance(Class<T> clazz)
	{
		System.out.println("0");
		ContextBuilder contextBuilder = ContextBuilder.newBuilder(provider);
		System.out.println("1");
		contextBuilder.credentials(username, apiKey);
		System.out.println("3");

		return contextBuilder.buildApi(clazz);
	}

	public <T extends View> T getViewInstance(Class<T> clazz)
	{
		return ContextBuilder.newBuilder(provider).credentials(username, apiKey).buildView(clazz);
	}

	public NovaApi getNovaApiInstance()
	{
		return getApiInstance(NovaApi.class);
	}

	public ComputeService getComputeService()
	{
		ComputeServiceContext computeServiceContext = getViewInstance(ComputeServiceContext.class);

		return computeServiceContext.getComputeService();
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
