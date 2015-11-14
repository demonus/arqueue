package io.github.arqueue.api.beans.post.request.server;

import com.google.gson.annotations.SerializedName;
import io.github.arqueue.api.OpenStackConnection;
import io.github.arqueue.api.post.response.server.ServerResponse;
import io.github.arqueue.common.Result;
import io.github.arqueue.core.runners.Buildable;
import io.github.arqueue.exception.OpenStackApiException;
import io.github.arqueue.exception.ValidationException;

import java.util.Map;
import java.util.Set;

/**
 * Created by root on 11/10/15.
 */
public class ServerRequest implements Buildable
{
	private String imageRef;
	private String flavorRef;
	private String name;
	private String group;
	private String region;

	@SerializedName("config_drive")
	private String configDrive;

	@SerializedName("block_device_mapping_v2")
	private BlockDeviceMapping blockDeviceMapping;

	@SerializedName("key_name")
	private String keyName;

	@SerializedName("OS-DCF:diskConfig")
	private String diskConfig;

	private Map<String, String> metadata;

	private Set<Personality> personality;

	private Set<Network> networks;

	@Override
	public Result build(OpenStackConnection connection)
			throws ValidationException, OpenStackApiException
	{
		ServerResponse serverResponse = connection.createServer(this);

		return new Result(serverResponse);
	}

	public String getImageRef()
	{
		return imageRef;
	}

	public void setImageRef(String imageRef)
	{
		this.imageRef = imageRef;
	}

	public String getFlavorRef()
	{
		return flavorRef;
	}

	public void setFlavorRef(String flavorRef)
	{
		this.flavorRef = flavorRef;
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

	public String getConfigDrive()
	{
		return configDrive;
	}

	public void setConfigDrive(String configDrive)
	{
		this.configDrive = configDrive;
	}

	public BlockDeviceMapping getBlockDeviceMapping()
	{
		return blockDeviceMapping;
	}

	public void setBlockDeviceMapping(BlockDeviceMapping blockDeviceMapping)
	{
		this.blockDeviceMapping = blockDeviceMapping;
	}

	public String getKeyName()
	{
		return keyName;
	}

	public void setKeyName(String keyName)
	{
		this.keyName = keyName;
	}

	public String getDiskConfig()
	{
		return diskConfig;
	}

	public void setDiskConfig(String diskConfig)
	{
		this.diskConfig = diskConfig;
	}

	public Map<String, String> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata)
	{
		this.metadata = metadata;
	}

	public Set<Network> getNetworks()
	{
		return networks;
	}

	public void setNetworks(Set<Network> networks)
	{
		this.networks = networks;
	}

	public Set<Personality> getPersonality()
	{
		return personality;
	}

	public void setPersonality(Set<Personality> personality)
	{
		this.personality = personality;
	}
}
