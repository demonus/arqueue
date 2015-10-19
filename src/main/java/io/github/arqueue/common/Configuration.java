package io.github.arqueue.common;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by root on 10/16/15.
 */
public class Configuration
{
	private Logger logger = Logger.getLogger(Configuration.class);

	private static volatile Configuration instance;

	private Properties properties;

	private Configuration()
	{
		try
		{
			properties = Properties.load("./conf/server.conf");

			String instanceId = properties.getProperty("instance-id");
			String mysqlPassword = properties.getProperty("hibernate.connection.password", "");

			boolean save = false;

			if (instanceId == null || "".equals(instanceId))
			{
				instanceId = UUID.randomUUID().toString();

				properties.setProperty("instance-id", instanceId);

				save = true;
			}

			if ("true".equalsIgnoreCase(properties.getProperty("protect-passwords", "false")))
			{
				properties.setProperty("hibernate.connection.password", "");

				save = true;
			}

			if (save)
			{
				properties.save();

				properties.setProperty("hibernate.connection.password", mysqlPassword);
			}
		}
		catch (IOException ioEx)
		{
			logger.error("Could not load configuration. Application will be terminated.\n" +
					Utils.getExceptionStackTrace(ioEx));

			Runtime.getRuntime().exit(1);
		}
	}

	public static Configuration getInstance()
	{
		if (instance == null)
		{
			synchronized (Configuration.class)
			{
				if (instance == null)
				{
					instance = new Configuration();
				}
			}
		}

		return instance;
	}

	public String getInstanceId()
	{
		return properties.getProperty("instance-id");
	}

	public int getMaxParalllelJobs()
	{
		return Integer.parseInt(properties.getProperty("max-parallel-jobs", "10"));
	}

	public int getMinIdleThreads()
	{
		return Integer.parseInt(properties.getProperty("idle-thread-count", "3"));
	}

	public int getMaxThreads()
	{
		return Integer.parseInt(properties.getProperty("max-thread-count", "10"));
	}

	public int getSchedulerCheckInterval()
	{
		return Integer.parseInt(properties.getProperty("check-interval-seconds", "10")) * 1000;
	}

	public Properties getSubset(String prefix)
	{
		Properties res = new Properties();

		for (Map.Entry<Object, Object> entry : properties.entrySet())
		{
			String key = entry.getKey().toString();

			if (key != null && prefix != null && key.length() >= prefix.length() &&
					key.substring(0, prefix.length()).equalsIgnoreCase(prefix))
			{
				res.setProperty(key, entry.getValue().toString());
			}
		}

		return res;
	}
}
