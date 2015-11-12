package io.github.arqueue.api.beans.users;

import java.util.Set;

/**
 * Created by root on 11/11/15.
 */
public class Service
{
	private String name;

	private String type;

	private Set<Endpoint> endpoints;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public Set<Endpoint> getEndpoints()
	{
		return endpoints;
	}

	public void setEndpoints(Set<Endpoint> endpoints)
	{
		this.endpoints = endpoints;
	}
}
