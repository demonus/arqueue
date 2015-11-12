package io.github.arqueue.api.beans.users;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

/**
 * Created by root on 11/11/15.
 */
public class Access
{
	private Token token;

	@SerializedName("RAX-AUTH:authenticatedBy")
	private Set<String> authenticatedBy;

	public Token getToken()
	{
		return token;
	}

	public void setToken(Token token)
	{
		this.token = token;
	}

	public Set<String> getAuthenticatedBy()
	{
		return authenticatedBy;
	}

	public void setAuthenticatedBy(Set<String> authenticatedBy)
	{
		this.authenticatedBy = authenticatedBy;
	}

	private Set<Service> serviceCatalog;

	private User user;

	public Set<Service> getServiceCatalog()
	{
		return serviceCatalog;
	}

	public void setServiceCatalog(Set<Service> serviceCatalog)
	{
		this.serviceCatalog = serviceCatalog;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}
