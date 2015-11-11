package io.github.arqueue.api.jcloud;

import io.github.arqueue.exception.ValidationException;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.options.TemplateOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 11/10/15.
 */
public class JCloudServer implements JCloudBuildable
{
	String image;
	String hardware;
	String name;
	String group;
	String region;

	@Override
	public Set<? extends NodeMetadata> build(InstanceGenerator instanceGenerator)
			throws RunNodesException, ValidationException
	{
		Hardware hwr = JCloudUtils.getHardware(instanceGenerator, hardware);

		if (hwr != null)
		{
			Image img = instanceGenerator.getComputeService().getImage(region + "/" + image);

			if (img != null)
			{
				TemplateOptions options = new TemplateOptions();

				Set<String> names = new HashSet<>();

				names.add(name);

				options.nodeNames(names);

				Template template =
						instanceGenerator.getComputeService().templateBuilder().locationId(region).fromHardware(hwr)
								.fromImage(img).options(options).build();

				return instanceGenerator.getComputeService().createNodesInGroup(group, 1,
						template);

			}
		}

		throw new ValidationException("Provided environment IDs not found");
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getHardware()
	{
		return hardware;
	}

	public void setHardware(String hardware)
	{
		this.hardware = hardware;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
