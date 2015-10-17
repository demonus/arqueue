package io.github.arqueue.client;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.api.beans.ServerDetails;
import io.github.arqueue.api.beans.ServerDetailsArray;
import io.github.arqueue.common.Configuration;
import io.github.arqueue.exception.AuthenticationException;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args) throws AuthenticationException
	{
		Configuration configuration = Configuration.getInstance();

		RackspaceConnect rackConnect = new RackspaceConnect();

		rackConnect.login("longjump", args[0]);

		ServerDetailsArray serverDetailsArray = rackConnect.getServerDetails();

		for (ServerDetails serverDetails : serverDetailsArray.getServers())
		{
			System.out.println(serverDetails.getId() + " " + serverDetails.getName());
		}
	}
}
