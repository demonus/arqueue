package com.arqueue.client;

import com.arqueue.api.RackspaceConnect;
import com.arqueue.api.beans.ServerDetails;
import com.arqueue.api.beans.ServerDetailsArray;
import com.arqueue.exception.AuthenticationException;

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
