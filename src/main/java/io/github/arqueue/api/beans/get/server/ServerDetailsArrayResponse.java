package io.github.arqueue.api.beans.get.server;

/**
 * Created by root on 10/14/15.
 */
public class ServerDetailsArrayResponse
{
	ServerDetailsResponse[] servers;

	public ServerDetailsResponse[] getServers()
	{
		return servers;
	}

	public void setServers(ServerDetailsResponse[] servers)
	{
		this.servers = servers;
	}
}
