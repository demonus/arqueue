package io.github.arqueue.api.post.response.server;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 11/13/15.
 */
public class Link
{
	@SerializedName("ref")
	private String url;

	@SerializedName("rel")
	private String description;

	private String type;

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
