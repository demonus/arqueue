package io.github.arqueue.core.runners.beans;

import io.github.arqueue.api.jcloud.JCloudUtils;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;

import java.util.Set;

/**
 * Created by remote on 10/29/15.
 */
public class Server
{
	private String hardware;

	private String image;

	private String group;

	private String region;

	private int count;

	public String getHardware()
	{
		return hardware;
	}

	public void setHardware(String hardware)
	{
		this.hardware = hardware;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public NodeMetadata buildSingle(ComputeService computeService) throws RunNodesException
	{
		if (count > 1)
		{
			throw new IllegalStateException("Set Expected. Use build() method.");
		}
		else
		{
			return build(computeService).iterator().next();
		}
	}

	public Set<? extends NodeMetadata> build(ComputeService computeService) throws RunNodesException
	{
		Hardware hwr = JCloudUtils.getHardware(computeService, hardware);

		if (hwr != null)
		{
			Image img = computeService.getImage(image);

			if (img != null)
			{
				Template template =
						computeService.templateBuilder().locationId(region).fromHardware(hwr).fromImage(img).build();

				return computeService.createNodesInGroup(group, count,
						template);

			}
		}

		return null;
	}
}
