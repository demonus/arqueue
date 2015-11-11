package io.github.arqueue.api.jcloud;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.arqueue.common.Utils;
import io.github.arqueue.exception.CacheException;
import io.github.arqueue.hibernate.beans.User;
import org.apache.log4j.Logger;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.internal.ImageImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 11/9/15.
 */
public class JCloudUtils
{
	private static final Logger logger = Logger.getLogger(JCloudUtils.class);

	private static Gson gson;

	static
	{
		gson = new Gson();
	}

	public static Set<? extends Hardware> listHardware(InstanceGenerator instanceGenerator)
	{
		Set<? extends Hardware> hardware =
				StaticDataCache.getInstance()
						.getFromCache(Utils.buildCacheKey(instanceGenerator.getUser().getId(), "hardware"));

		if (hardware == null)
		{
			ComputeService computeService = instanceGenerator.getComputeService();

			hardware = computeService.listHardwareProfiles();

			if (hardware != null)
			{
				try
				{
					StaticDataCache.getInstance()
							.addToCache(Utils.buildCacheKey(instanceGenerator.getUser().getId(), "hardware"),
									hardware);
				}
				catch (CacheException e)
				{
					logger.error("Could not save Hardware to cache: " + Utils.getExceptionStackTrace(e));
				}
			}
		}

		return hardware;
	}

	public static Set<? extends Image> listSnapshotImages(InstanceGenerator instanceGenerator)
	{
		Set<ImageImpl> images = new HashSet<>();

		for (Image im : listImages(instanceGenerator))
		{
			if ("snapshot".equals(im.getUserMetadata().get("image_type")))
			{
				images.add((ImageImpl) im);
			}
		}

		return images;
	}


	public static Set<? extends Image> listImages(InstanceGenerator instanceGenerator)
	{
		Set<? extends Image> images =
				StaticDataCache.getInstance().getFromCache(Utils.buildCacheKey(instanceGenerator.getUser().getId(),
						"images"));

		if (images == null)
		{
			ComputeService computeService = instanceGenerator.getComputeService();

			images = computeService.listImages();

			if (images != null)
			{
				try
				{
					StaticDataCache.getInstance()
							.addToCache(Utils.buildCacheKey(instanceGenerator.getUser().getId(), "images"), images);
				}
				catch (CacheException e)
				{
					logger.error("Could not save images to cache: " + Utils.getExceptionStackTrace(e));
				}
			}
		}

		return images;
	}

	public static <T extends JCloudBuildable> T parse(JsonElement json, Class<T> clazz)
	{
		return gson.fromJson(json, clazz);
	}

	public static Hardware getHardware(InstanceGenerator instanceGenerator, String id)
	{
		Set<? extends Hardware> hardwares = listHardware(instanceGenerator);

		for (Hardware hw : hardwares)
		{
			if (hw.getProviderId().equals(id))
			{
				return hw;
			}
		}

		return null;
	}
}
