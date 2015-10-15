package com.rackspace.connect.api.beans;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by root on 10/14/15.
 */
public class ServerDetailsArray
{
	ServerDetails[] servers;

	public ServerDetails[] getServers()
	{
		return servers;
	}

	public void setServers(ServerDetails[] servers)
	{
		this.servers = servers;
	}
}
