package io.github.arqueue.api.beans.flavors;

import java.util.Set;

/**
 * Created by root on 11/11/15.
 */
public class Flavor
{
	private String id;

	private String name;

	private Set<Links> links;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Set<Links> getLinks()
	{
		return links;
	}

	public void setLinks(Set<Links> links)
	{
		this.links = links;
	}
}
