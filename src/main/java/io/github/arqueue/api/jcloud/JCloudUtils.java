package io.github.arqueue.api.jcloud;

import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Hardware;

import java.util.Set;

/**
 * Created by remote on 10/29/15.
 */
public class JCloudUtils
{

	public static Hardware getHardware(ComputeService computeService, String providerId)
	{
		if (providerId != null && !"".equals(providerId))
		{
			Set<? extends Hardware> hardwares = computeService.listHardwareProfiles();

			for (Hardware hardware : hardwares)
			{
				if (providerId.equals(hardware.getProviderId()))
				{
					return hardware;
				}
			}
		}

		return null;
	}
}
