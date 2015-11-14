package io.github.arqueue.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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

	private static Gson gson;

	static
	{
		gson = new Gson();
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

	public static <T> T parse(JsonElement json, Class<T> clazz)
	{
		return gson.fromJson(json, clazz);
	}

	public static <T> T parse(String json, Class<T> clazz)
	{
		return gson.fromJson(json, clazz);
	}

	public static <T extends JsonElement> T toJsonElement(Object object)
	{
		return (T) gson.toJsonTree(object);
	}
}
