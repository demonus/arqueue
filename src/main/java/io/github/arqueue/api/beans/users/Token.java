package io.github.arqueue.api.beans.users;

/**
 * Created by root on 11/11/15.
 */
public class Token
{
	private String id;

	private String expires;

	private Tenant tenant;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getExpires()
	{
		return expires;
	}

	public void setExpires(String expires)
	{
		this.expires = expires;
	}

	public Tenant getTenant()
	{
		return tenant;
	}

	public void setTenant(Tenant tenant)
	{
		this.tenant = tenant;
	}
}
