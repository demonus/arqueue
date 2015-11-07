package io.github.arqueue.client;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.api.beans.ServerDetails;
import io.github.arqueue.api.beans.ServerDetailsArray;
import io.github.arqueue.api.jcloud.InstanceGenerator;
import io.github.arqueue.api.jcloud.StaticDataCache;
import io.github.arqueue.exception.AuthenticationException;
import io.github.arqueue.exception.CacheException;
import io.github.arqueue.hibernate.SessionFactory;
import io.github.arqueue.hibernate.beans.Action;
import io.github.arqueue.hibernate.beans.Flow;
import io.github.arqueue.hibernate.beans.Task;
import io.github.arqueue.hibernate.beans.User;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Image;
import org.jclouds.openstack.nova.v2_0.NovaApi;

import java.util.Set;

/**
 * Created by root on 10/14/15.
 */
public class RackClient
{
	public static void main(String[] args) throws AuthenticationException, InterruptedException, CacheException
	{
		PropertyConfigurator.configure("./conf/log4j.properties");

		RackspaceConnect rackConnect = new RackspaceConnect();

		StaticDataCache cache = new StaticDataCache(10, 300);

		cache.addToCache("longjump", rackConnect);

		RackspaceConnect rackspaceConnect = cache.getFromCache("longjump");

		System.out.println(rackspaceConnect);
	}
}
