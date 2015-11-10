package io.github.arqueue.api.jcloud;

import io.github.arqueue.common.Utils;
import io.github.arqueue.exception.CacheException;
import io.github.arqueue.hibernate.beans.User;
import org.apache.log4j.Logger;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Hardware;

import java.util.Set;

/**
 * Created by root on 11/9/15.
 */
public class JCloudUtils
{
	private static final Logger logger = Logger.getLogger(JCloudUtils.class);

	public static Set<? extends Hardware> listHardware(InstanceGenerator instanceGenerator, User user)
	{
		Set<? extends Hardware> hardware =
				StaticDataCache.getInstance().getFromCache(Utils.buildCacheKey(user.getId(), "hardware"));

		if (hardware == null)
		{
			if (instanceGenerator == null)
			{
				instanceGenerator = new InstanceGenerator(user);
			}

			ComputeService computeService = instanceGenerator.getComputeService();

			hardware = computeService.listHardwareProfiles();

			if (hardware != null)
			{
				try
				{
					StaticDataCache.getInstance().addToCache(Utils.buildCacheKey(user.getId(), "hardware"), hardware);
				}
				catch (CacheException e)
				{
					logger.error("Could not save Hardware to log: " + Utils.getExceptionStackTrace(e));
				}
			}
		}

		return hardware;
	}
}
