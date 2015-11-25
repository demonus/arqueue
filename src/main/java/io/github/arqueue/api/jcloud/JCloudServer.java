package io.github.arqueue.api.jcloud;

import io.github.arqueue.api.RackspaceConnect;
import io.github.arqueue.common.Result;
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
	public Result build(RackspaceConnect connect)
			throws RunNodesException, ValidationException
	{

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

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public String getRegion()
	{
		return region;
	}

	public void setRegion(String region)
	{
		this.region = region;
	}
}
