package io.github.arqueue.api.beans.get.flavor;

import java.util.Set;

/**
 * Created by root on 11/12/15.
 */
public class Flavors
{
	private Set<Flavor> flavors;

	public Set<Flavor> getFlavors()
	{
		return flavors;
	}

	public void setFlavors(Set<Flavor> flavors)
	{
		this.flavors = flavors;
	}
}
