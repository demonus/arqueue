package com.rackspace.connect.client;

import com.rackspace.connect.api.RackspaceConnect;
import com.rackspace.connect.api.beans.ServerDetails;
import com.rackspace.connect.api.beans.ServerDetailsArray;
import com.rackspace.connect.exception.AuthenticationException;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args) throws AuthenticationException
	{
		RackspaceConnect rackConnect = new RackspaceConnect();

		rackConnect.login("longjump", "ce14a69d2c20adc0659d7875560e4322");

		ServerDetailsArray serverDetailsArray = rackConnect.getServerDetails();

		for (ServerDetails serverDetails : serverDetailsArray.getServers())
		{
			System.out.println(serverDetails.getId() + " " + serverDetails.getName());
		}
	}
}
