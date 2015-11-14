package io.github.arqueue.api.beans.post.request.server;

/**
 * Created by root on 11/13/15.
 */
public class Network
{
	private String uuid;

	private String port;

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}
}
