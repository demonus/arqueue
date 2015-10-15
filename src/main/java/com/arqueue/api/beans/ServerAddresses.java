package com.arqueue.api.beans;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by root on 10/14/15.
 */
public class ServerAddresses
{
	@XmlElement(name = "public")
	String[] publicIp;

	@XmlElement(name = "private")
	String[] privateIp;

	public String[] getPublicIp()
	{
		return publicIp;
	}

	public void setPublicIp(String[] publicIp)
	{
		this.publicIp = publicIp;
	}

	public String[] getPrivateIp()
	{
		return privateIp;
	}

	public void setPrivateIp(String[] privateIp)
	{
		this.privateIp = privateIp;
	}
}
