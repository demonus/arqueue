package io.github.arqueue.common;

import io.github.arqueue.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.io.Reader;
import java.io.Writer;

/**
 * Created by root on 10/16/15.
 */
public class Utils
{
	private static Logger logger = Logger.getLogger(Utils.class);


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
					else if (resource instanceof Session)
					{
						((Session) resource).close();
					}
					else if (resource instanceof SessionFactory)
					{
						((SessionFactory) resource).close();
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

	public static String buildCacheKey(String... values)
	{
		StringBuilder stringBuilder = new StringBuilder();

		boolean first = true;

		for (String value : values)
		{
			if (!first)
			{
				stringBuilder.append("|");
			}
			else
			{
				first = false;
			}

			stringBuilder.append(value);
		}

		return stringBuilder.toString();
	}
}
