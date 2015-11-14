package io.github.arqueue.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.arqueue.api.beans.get.flavor.Flavors;
import io.github.arqueue.api.beans.get.image.Image;
import io.github.arqueue.api.beans.get.image.Images;
import io.github.arqueue.api.beans.get.server.ServerDetailsArrayResponse;
import io.github.arqueue.api.beans.get.login.Endpoint;
import io.github.arqueue.api.beans.get.login.LoginResponse;
import io.github.arqueue.api.beans.get.login.Service;
import io.github.arqueue.api.beans.post.request.server.ServerRequest;
import io.github.arqueue.api.post.response.server.ServerResponse;
import io.github.arqueue.common.Utils;
import io.github.arqueue.exception.AuthenticationException;
import io.github.arqueue.exception.OpenStackApiException;
import io.github.arqueue.exception.ValidationException;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by root on 10/14/15.
 */
public class OpenStackConnection
{
	public static final String ALIAS_SERVER = "cloudServersOpenStack";

	private ClientConfig clientConfig;

	private Client client;

	private JsonParser parser;

	private String token = null;

	private String account = null;

	private String defaultRegion;

	private Map<String, Map<String, Endpoint>> targets = new HashMap<>();

	public OpenStackConnection()
	{
		clientConfig = new ClientConfig();

		client = ClientBuilder.newClient(clientConfig);

		parser = new JsonParser();
	}

	public void login(String username, String apiKey) throws AuthenticationException
	{
		JsonObject keyCredentials = new JsonObject();

		keyCredentials.addProperty("username", username);
		keyCredentials.addProperty("apiKey", apiKey);

		JsonObject auth = new JsonObject();

		auth.add("RAX-KSKEY:apiKeyCredentials", keyCredentials);

		JsonObject login = new JsonObject();

		login.add("auth", auth);

		WebTarget target = client.target("https://identity.api.rackspacecloud.com").path("v2.0").path("tokens");

		Response res = target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(login.toString()));

		if (res.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
		{
			throw new AuthenticationException("Could not login: " + res.getStatusInfo().getReasonPhrase());
		}

		String result = res.readEntity(String.class);

		LoginResponse loginResponse = Utils.parse(result, LoginResponse.class);

		token = loginResponse.getAccess().getToken().getId();

		account = loginResponse.getAccess().getToken().getTenant().getId();

		defaultRegion = loginResponse.getAccess().getUser().getDefaultRegion();

		for (Service service : loginResponse.getAccess().getServiceCatalog())
		{
			Map<String, Endpoint> endpoints = new HashMap<>(service.getEndpoints().size());

			for (Endpoint endpoint : service.getEndpoints())
			{
				endpoints.put(endpoint.getRegion(), endpoint);
			}

			targets.put(service.getName(), endpoints);
		}
	}

	private void checkStatus()
	{
		if (token == null)
		{
			throw new IllegalStateException("Not logged in!");
		}
	}

	public ServerResponse createServer(ServerRequest serverRequest)
			throws OpenStackApiException, ValidationException
	{
		return createServer(serverRequest, defaultRegion);
	}

	public ServerResponse createServer(ServerRequest serverRequest, String region)
			throws OpenStackApiException, ValidationException
	{
		JsonObject jsonObject = new JsonObject();

		jsonObject.add("server", Utils.toJsonElement(serverRequest));

		return submitRequest(ServerResponse.class, null, null, jsonObject.toString(),
				OpenStackConnection.ALIAS_SERVER, region, true, "servers");
	}

	public ServerDetailsArrayResponse getServerDetails() throws OpenStackApiException, ValidationException
	{
		return getServerDetails(defaultRegion);
	}

	public ServerDetailsArrayResponse getServerDetails(String region) throws OpenStackApiException, ValidationException
	{
		checkStatus();

		ServerDetailsArrayResponse serverDetailsArrayResponse =
				submitRequest(ServerDetailsArrayResponse.class, null, null, null, OpenStackConnection.ALIAS_SERVER,
						region,
						false,
						"servers", "detail");

		return serverDetailsArrayResponse;
	}

	public Flavors listFlavors() throws OpenStackApiException, ValidationException
	{
		return listFlavors(null);
	}

	public Flavors listFlavors(String region) throws OpenStackApiException, ValidationException
	{
		Flavors flavors =
				submitRequest(Flavors.class, null, null, null, OpenStackConnection.ALIAS_SERVER, region, false,
						"flavors");

		return flavors;
	}

	public String getServersUrl()
	{
		return getServersUrl(null);
	}

	public String getServersUrl(String region)
	{
		return getUrl(OpenStackConnection.ALIAS_SERVER, region);
	}

	private String getUrl(String alias, String region)
	{
		if (targets.containsKey(alias))
		{
			Map<String, Endpoint> links = targets.get(alias);

			if (links.containsKey(region))
			{
				return links.get(region).getPublicURL();
			}
		}

		return null;
	}


	private <T> T submitRequest(Class<T> clazz, Map<String, String> headers, Map<String, String> params, String body,
								String targetAlias,
								String region, boolean post, String... uriPathElements)
			throws OpenStackApiException, ValidationException
	{
		Invocation.Builder request = getRequest(headers, post ? null : params, targetAlias, region, uriPathElements);

		Response response;

		if (body != null && post)
		{
			response = request.post(Entity.json(body));
		}
		else if (post)
		{
			Form form = new Form();

			for (Map.Entry<String, String> entry : params.entrySet())
			{
				form.param(entry.getKey(), entry.getValue());
			}

			response = request.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		}
		else
		{
			response = request.get();
		}

		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
		{
			throw new OpenStackApiException("Could not process request: " + response.getStatusInfo().getReasonPhrase
					());
		}

		T result = Utils.parse(response.readEntity(String.class), clazz);

		return result;
	}

	public Images listImages() throws OpenStackApiException, ValidationException
	{
		return listImages(null);
	}

	public Images listSnapshotImages() throws OpenStackApiException, ValidationException
	{
		return listSnapshotImages(null);
	}

	public Images listSnapshotImages(String region) throws OpenStackApiException, ValidationException
	{
		Images images = listImages(region);

		Iterator<Image> iterator = images.getImages().iterator();

		while (iterator.hasNext())
		{
			Image image = iterator.next();

			if (image.getMetadata() == null || !image.getMetadata().containsKey("image_type"))
			{
				iterator.remove();
			}
			else if (!"snapshot".equals(image.getMetadata().get("image_type")))
			{
				iterator.remove();
			}
		}

		return images;
	}

	public Images listImages(String region) throws OpenStackApiException, ValidationException
	{
		return submitRequest(Images.class, null, null, null, OpenStackConnection.ALIAS_SERVER, region, false, "images",
				"detail");
	}

	private Invocation.Builder getRequest(Map<String, String> headers, Map<String, String> params,
										  String targetAlias,
										  String region, String... uriPathElements) throws ValidationException
	{
		checkStatus();

		if (region == null)
		{
			region = defaultRegion;
		}

		String url = getUrl(targetAlias, (region == null) ? defaultRegion : region);

		if (url == null)
		{
			throw new ValidationException(
					"Endpoint public URL not found for given alias/region: " + targetAlias + "/" + region);
		}

		WebTarget target = client.target(url);

		for (String uriPathElement : uriPathElements)
		{
			target = target.path(uriPathElement);
		}

		Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-Auth-Token", token);

		if (headers != null)
		{
			for (Map.Entry<String, String> header : headers.entrySet())
			{
				request.header(header.getKey(), header.getValue());
			}
		}

		if (params != null)
		{
			for (Map.Entry<String, String> param : params.entrySet())
			{
				request.property(param.getKey(), param.getValue());
			}
		}

		return request;
	}

	public String getDefaultRegion()
	{
		return defaultRegion;
	}

	public void setDefaultRegion(String defaultRegion)
	{
		this.defaultRegion = defaultRegion;
	}
}
