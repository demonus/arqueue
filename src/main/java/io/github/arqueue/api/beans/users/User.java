package io.github.arqueue.api.beans.users;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlElement;
import java.util.Set;

/**
 * Created by root on 11/11/15.
 */
public class User
{
	private String id;

	private Set<Role> roles;

	private String name;

	@SerializedName("RAX-AUTH:defaultRegion")
	private String defaultRegion;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDefaultRegion()
	{
		return defaultRegion;
	}

	public void setDefaultRegion(String defaultRegion)
	{
		this.defaultRegion = defaultRegion;
	}
}
