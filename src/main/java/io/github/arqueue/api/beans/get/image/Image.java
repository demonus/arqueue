package io.github.arqueue.api.beans.get.image;

import com.google.gson.annotations.SerializedName;
import io.github.arqueue.api.post.response.server.Link;

import java.util.Map;
import java.util.Set;

/**
 * Created by root on 11/13/15.
 */
public class Image
{
	private String id;

	private String status;

	private String updated;

	private Set<Link> links;

	@SerializedName("OS-DCF:diskConfig")
	private String diskConfig;

	@SerializedName("OS- EXT-IMG-SIZE:size")
	private String size;

	private String name;

	private String created;

	private String minDisk;

	private String progress;

	private String minRam;

	private Map<String, String> metadata;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getUpdated()
	{
		return updated;
	}

	public void setUpdated(String updated)
	{
		this.updated = updated;
	}

	public Set<Link> getLinks()
	{
		return links;
	}

	public void setLinks(Set<Link> links)
	{
		this.links = links;
	}

	public String getDiskConfig()
	{
		return diskConfig;
	}

	public void setDiskConfig(String diskConfig)
	{
		this.diskConfig = diskConfig;
	}

	public String getSize()
	{
		return size;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCreated()
	{
		return created;
	}

	public void setCreated(String created)
	{
		this.created = created;
	}

	public String getMinDisk()
	{
		return minDisk;
	}

	public void setMinDisk(String minDisk)
	{
		this.minDisk = minDisk;
	}

	public String getProgress()
	{
		return progress;
	}

	public void setProgress(String progress)
	{
		this.progress = progress;
	}

	public String getMinRam()
	{
		return minRam;
	}

	public void setMinRam(String minRam)
	{
		this.minRam = minRam;
	}

	public Map<String, String> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata)
	{
		this.metadata = metadata;
	}
}
