package io.github.arqueue.core.runners;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.arqueue.api.beans.post.request.server.ServerRequest;
import io.github.arqueue.common.Utils;
import io.github.arqueue.hibernate.beans.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/23/15.
 */
public class TaskData
{
	private String type;

	private String id;

	private Buildable node;

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

	public TaskData(String type, String id, Buildable node, HashMap<String, String> properties)
	{
		this.type = type;
		this.id = id;
		this.node = node;
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

	public static TaskData parse(Task task)
	{
		String json = task.getData();

		JsonParser parser = new JsonParser();

		JsonObject object = (JsonObject) parser.parse(json);

		String id = object.get("id").getAsString();

		String type = object.get("type").getAsString();

		Buildable buildable = null;

		if ("SERVER".equalsIgnoreCase(type))
		{
			buildable = Utils.parse(object.get("server"), ServerRequest.class);
		}

		JsonElement propertiesElement = object.get("properties");

		HashMap<String, String> properties;

		if (propertiesElement != null)
		{
			JsonObject propObject = propertiesElement.getAsJsonObject();

			properties = new HashMap<>(propObject.entrySet().size());

			for (Map.Entry<String, JsonElement> entry : propObject.entrySet())
			{
				properties.put(entry.getKey(), entry.getValue().getAsString());
			}
		}
		else
		{
			properties = new HashMap<>();
		}

		return new TaskData(id, type, buildable, properties);
	}

	public Buildable getNode()
	{
		return node;
	}

	public void setNode(Buildable node)
	{
		this.node = node;
	}
}
