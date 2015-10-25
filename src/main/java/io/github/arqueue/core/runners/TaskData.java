package io.github.arqueue.core.runners;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/23/15.
 */
public class TaskData
{
	private String type;

	private String id;

	private HashMap<String, String> properties;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public HashMap<String, String> getProperties()
	{
		return properties;
	}

	public void setProperties(HashMap<String, String> properties)
	{
		this.properties = properties;
	}

	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder().append("Id = ").append(id).append("\nType = ").append(type)
				.append("\nProperties:\n");

		if (properties != null)
		{
			for (Map.Entry<String, String> entry : properties.entrySet())
			{
				stringBuilder.append(entry.getKey()).append(" = \"").append(entry.getValue()).append("\"");
			}
		}

		return stringBuilder.toString();
	}
}
