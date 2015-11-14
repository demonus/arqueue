package io.github.arqueue.api.beans.post.request.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 11/13/15.
 */
public class BlockDeviceMapping
{
	@SerializedName("boot_index")
	private String bootIndex;

	private String uuid;

	@SerializedName("source_type")
	private String sourceType;

	@SerializedName("destination_type")
	private String destinationType;

	@SerializedName("delete_on_termination")
	private String deleteOnTermination;

	public String getBootIndex()
	{
		return bootIndex;
	}

	public void setBootIndex(String bootIndex)
	{
		this.bootIndex = bootIndex;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getSourceType()
	{
		return sourceType;
	}

	public void setSourceType(String sourceType)
	{
		this.sourceType = sourceType;
	}

	public String getDestinationType()
	{
		return destinationType;
	}

	public void setDestinationType(String destinationType)
	{
		this.destinationType = destinationType;
	}

	public String getDeleteOnTermination()
	{
		return deleteOnTermination;
	}

	public void setDeleteOnTermination(String deleteOnTermination)
	{
		this.deleteOnTermination = deleteOnTermination;
	}
}
