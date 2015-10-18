package io.github.arqueue.common;

/**
 * Created by root on 10/17/15.
 */
public class Environment
{
	private static Configuration configuration;

	static
	{
		configuration = Configuration.getInstance();
	}

	public static Configuration getConfiguration()
	{
		return configuration;
	}
}
