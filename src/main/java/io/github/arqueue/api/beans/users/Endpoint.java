package io.github.arqueue.api.beans.users;

/**
 * Created by root on 11/11/15.
 */
public class Endpoint
{
	private String region;

	private String tenantId;

	private String publicURL;

	private String internalURL;

	private String versionInfo;

	private String versionList;

	private String versionId;

	public String getRegion()
	{
		return region;
	}

	public void setRegion(String region)
	{
		this.region = region;
	}

	public String getTenantId()
	{
		return tenantId;
	}

	public void setTenantId(String tenantId)
	{
		this.tenantId = tenantId;
	}

	public String getPublicURL()
	{
		return publicURL;
	}

	public void setPublicURL(String publicURL)
	{
		this.publicURL = publicURL;
	}

	public String getInternalURL()
	{
		return internalURL;
	}

	public void setInternalURL(String internalURL)
	{
		this.internalURL = internalURL;
	}

	public String getVersionInfo()
	{
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo)
	{
		this.versionInfo = versionInfo;
	}

	public String getVersionList()
	{
		return versionList;
	}

	public void setVersionList(String versionList)
	{
		this.versionList = versionList;
	}

	public String getVersionId()
	{
		return versionId;
	}

	public void setVersionId(String versionId)
	{
		this.versionId = versionId;
	}
}
