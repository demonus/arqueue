package io.github.arqueue.client;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.api.beans.ServerDetails;
import io.github.arqueue.api.beans.ServerDetailsArray;
import io.github.arqueue.exception.AuthenticationException;

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
