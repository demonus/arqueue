package io.github.arqueue.common;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.Reader;
import java.io.Writer;

/**
 * Created by root on 10/16/15.
 */
public class Utils
{
	private static Logger logger = Logger.getLogger(Utils.class);

	static {
		PropertyConfigurator.configure("./conf/log4j.properties");
	}

	public static void closeResources(Object... resources)
	{
		for (Object resource : resources)
		{
			if (resource != null)
			{
				try
				{
					if (resource instanceof Reader)
					{
						((Reader) resource).close();
					}
					else if (resource instanceof Writer)
					{
						((Writer) resource).close();
					}
				}
				catch (Exception ex)
				{
					logger.error("Could not close resource " + resource.getClass().getName() + ": " +
							Utils.getExceptionStackTrace(ex));
				}
			}
		}
	}

	public static String getExceptionStackTrace(Throwable t)
	{
		String res = "";

		boolean first = true;

		for (StackTraceElement stackTraceElement : t.getStackTrace())
		{
			if (!first)
			{
				res += "\n";
			}
			else
			{
				first = false;
			}

			res += stackTraceElement.toString();
		}

		return res;
	}
}
