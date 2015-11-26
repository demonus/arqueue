package io.github.arqueue.common;

import io.github.arqueue.api.OpenStackConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 11/25/15.
 */
public class RequestBuilder
{
	private Map<String, String> headers = new HashMap<>();

	private Map<String, String> parameters = new HashMap<>();

	private String alias = null;

	private String region = null;

	private String entity = null;

	private List<String> paths = new ArrayList<>();

	private OpenStackConnection.HttpMethod method = OpenStackConnection.HttpMethod.GET;

	public RequestBuilder(String region)
	{
		this.region = region;
	}

	public RequestBuilder region(String region)
	{
		this.region = region;

		return this;
	}

	public RequestBuilder alias(String alias)
	{
		this.alias = alias;

		return this;
	}

	public RequestBuilder header(String name, String value)
	{
		headers.put(name, value);

		return this;
	}

	public RequestBuilder parameter(String name, String value)
	{
		parameters.put(name, value);

		return this;
	}

	public RequestBuilder path(String value)
	{
		paths.add(value);

		return this;
	}

	public RequestBuilder entity(String entity)
	{
		this.entity = entity;

		return this;
	}

	public RequestBuilder method(OpenStackConnection.HttpMethod method)
	{
		this.method = method;

		return this;
	}

	public Map<String, String> getHeaders()
	{
		return headers;
	}

	public Map<String, String> getParameters()
	{
		return parameters;
	}

	public String getAlias()
	{
		return alias;
	}

	public String getRegion()
	{
		return region;
	}

	public String getEntity()
	{
		return entity;
	}

	public List<String> getPaths()
	{
		return paths;
	}

	public OpenStackConnection.HttpMethod getMethod()
	{
		return method;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("RequestBuilder{");
		sb.append("headers: {").append(Utils.mapToString(headers)).append("}");
		sb.append(", parameters: {").append(Utils.mapToString(parameters)).append("}");
		sb.append(", alias = '").append(alias).append('\'');
		sb.append(", region = '").append(region).append('\'');
		sb.append(", entity = '").append(entity).append('\'');
		sb.append(", paths: [").append(Utils.collectionToString(paths)).append("]");
		sb.append(", method = ").append(method);
		sb.append('}');
		return sb.toString();
	}

	public boolean isEntityBased()
	{
		return (entity != null &&
				Utils.in(method, OpenStackConnection.HttpMethod.POST, OpenStackConnection.HttpMethod.PUT));
	}
}
