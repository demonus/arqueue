package io.github.arqueue.client;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.exception.AuthenticationException;
import org.hibernate.SessionFactory;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args) throws AuthenticationException
	{
		RackspaceConnect rackConnect = new RackspaceConnect();

		SessionFactory sessionFactory = new AnnotationConfiguratio

		/*rackConnect.login("longjump", args[0]);

		ServerDetailsArray serverDetailsArray = rackConnect.getServerDetails();

		for (ServerDetails serverDetails : serverDetailsArray.getServers())
		{
			System.out.println(serverDetails.getId() + " " + serverDetails.getName());
		}*/
	}
}
