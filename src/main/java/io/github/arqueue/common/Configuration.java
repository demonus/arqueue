package io.github.arqueue.common;

import org.apache.log4j.Logger;

import java.io.IOException;
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
			String mysqlPassword = properties.getProperty("mysql-password", "");

			boolean save = false;

			if (instanceId == null || "".equals(instanceId))
			{
				instanceId = UUID.randomUUID().toString();

				properties.setProperty("instance-id", instanceId);

				save = true;
			}

			if ("true".equalsIgnoreCase(properties.getProperty("protect-passwords", "false")))
			{
				properties.setProperty("mysql-password", "");

				save = true;
			}

			if (save)
			{
				properties.save();

				properties.setProperty("mysql-password", mysqlPassword);
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

	public String getDatabaseConnectionString()
	{
		return properties.getProperty("mysql-connection-string");
	}

	public String getDatabaseUser()
	{
		return properties.getProperty("mysql-user");
	}

	public String getDatabasePassword()
	{
		return properties.getProperty("mysql-password");
	}

	public int getConnectionPoolMinIdle()
	{
		return Integer.parseInt(properties.getProperty("mysql-min-idle-connections", "10"));
	}

	public int getConnectionPoolMax()
	{
		return Integer.parseInt(properties.getProperty("mysql-max-connections", "50"));
	}

	public String getConnectionPoolTestQuery()
	{
		return properties.getProperty("mysql-test-query");
	}

	public int getSchedulerCheckInterval()
	{
		return Integer.parseInt(properties.getProperty("check-interval-seconds", "10")) * 1000;
	}
}
