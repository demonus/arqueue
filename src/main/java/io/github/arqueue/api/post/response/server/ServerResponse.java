package io.github.arqueue.api.post.response.server;

import com.google.gson.annotations.SerializedName;
import io.github.arqueue.api.OpenStackConnection;
import io.github.arqueue.core.runners.IResponse;
import io.github.arqueue.exception.OpenStackApiException;

import java.util.Set;

/**
 * Created by root on 11/13/15.
 */
public class ServerResponse implements IResponse
{
	private String id;

	private Set<Link> links;

	private String adminPass;

	@SerializedName("OS-DCF:diskConfig")
	private String diskConfig;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Set<Link> getLinks()
	{
		return links;
	}

	public void setLinks(Set<Link> links)
	{
		this.links = links;
	}

	public String getAdminPass()
	{
		return adminPass;
	}

	public void setAdminPass(String adminPass)
	{
		this.adminPass = adminPass;
	}

	public String getDiskConfig()
	{
		return diskConfig;
	}

	public void setDiskConfig(String diskConfig)
	{
		this.diskConfig = diskConfig;
	}

	@Override
	public void checkStatus(OpenStackConnection connection) throws OpenStackApiException
	{

	}
}
