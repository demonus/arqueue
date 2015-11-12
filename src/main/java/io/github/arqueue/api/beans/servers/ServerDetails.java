package io.github.arqueue.api.beans.servers;

import java.util.Map;

/**
 * Created by root on 10/14/15.
 */
public class ServerDetails
{
	String progress;

	String id;

	String imageId;

	String flavorId;

	String status;

	String name;

	String hostId;

	ServerAddresses addresses;

	Map<String, String> metadata;

	public String getProgress()
	{
		return progress;
	}

	public void setProgress(String progress)
	{
		this.progress = progress;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getImageId()
	{
		return imageId;
	}

	public void setImageId(String imageId)
	{
		this.imageId = imageId;
	}

	public String getFlavorId()
	{
		return flavorId;
	}

	public void setFlavorId(String flavorId)
	{
		this.flavorId = flavorId;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getHostId()
	{
		return hostId;
	}

	public void setHostId(String hostId)
	{
		this.hostId = hostId;
	}

	public ServerAddresses getAddresses()
	{
		return addresses;
	}

	public void setAddresses(ServerAddresses addresses)
	{
		this.addresses = addresses;
	}

	public Map<String, String> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata)
	{
		this.metadata = metadata;
	}
}
