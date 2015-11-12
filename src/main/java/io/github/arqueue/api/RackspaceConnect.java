package io.github.arqueue.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.arqueue.api.beans.flavors.Flavor;
import io.github.arqueue.api.beans.flavors.Flavors;
import io.github.arqueue.api.beans.servers.ServerDetailsArray;
import io.github.arqueue.api.beans.users.Endpoint;
import io.github.arqueue.api.beans.users.LoginDetails;
import io.github.arqueue.api.beans.users.Service;
import io.github.arqueue.api.jcloud.JCloudUtils;
import io.github.arqueue.exception.AuthenticationException;
import io.github.arqueue.exception.OpenStackApiException;
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
import java.util.Map;
import java.util.Set;

/**
 * Created by root on 10/14/15.
 */
public class RackspaceConnect
{
	private ClientConfig clientConfig;

	private Client client;

	private JsonParser parser;

	private String token = null;

	private String account = null;

	private String defaultRegion;

	private Map<String, Map<String, Endpoint>> targets = new HashMap<>();

	public RackspaceConnect()
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

		if (res.getStatus() != 200)
		{
			throw new AuthenticationException("Could not login: " + res.getStatusInfo().getReasonPhrase());
		}

		String result = res.readEntity(String.class);

		LoginDetails loginDetails = JCloudUtils.parse(result, LoginDetails.class);

		token = loginDetails.getAccess().getToken().getId();

		account = loginDetails.getAccess().getToken().getTenant().getId();

		defaultRegion = loginDetails.getAccess().getUser().getDefaultRegion();

		for (Service service : loginDetails.getAccess().getServiceCatalog())
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


	public ServerDetailsArray getServerDetails() throws OpenStackApiException
	{
		return getServerDetails(defaultRegion);
	}

	public ServerDetailsArray getServerDetails(String region) throws OpenStackApiException
	{
		checkStatus();

		ServerDetailsArray serverDetailsArray =
				procesRequest(ServerDetailsArray.class, null, null, null, "cloudServersOpenStack", region, false,
						"servers", "detail");

		return serverDetailsArray;
	}

	public Flavors listFlavors() throws OpenStackApiException
	{
		return listFlavors(defaultRegion);
	}

	public Flavors listFlavors(String region) throws OpenStackApiException
	{
		Flavors flavors = procesRequest(Flavors.class, null, null, null, "cloudServersOpenStack", region, false,
				"flavors");

		return flavors;
	}


	private <T> T procesRequest(Class<T> clazz, Map<String, String> headers, Map<String, String> params, String body,
								String targetAlias,
								String region, boolean post, String... uriPathElements) throws OpenStackApiException
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

		if (response.getStatus() != 200)
		{
			throw new OpenStackApiException("Could not process request: " + response.getStatusInfo().getReasonPhrase
					());
		}

		T result = JCloudUtils.parse(response.readEntity(String.class), clazz);

		return result;
	}


	private Invocation.Builder getRequest(Map<String, String> headers, Map<String, String> params,
										  String targetAlias,
										  String region, String... uriPathElements)
	{
		checkStatus();

		if (region == null)
		{
			region = defaultRegion;
		}

		WebTarget target = client.target(targets.get(targetAlias).get(region).getPublicURL());

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
}
