package com.rackspace.connect.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rackspace.connect.api.beans.ServerDetails;
import com.rackspace.connect.api.beans.ServerDetailsArray;
import com.rackspace.connect.exception.AuthenticationException;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

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

		JsonObject response = (JsonObject) parser.parse(res.readEntity(String.class));

		if (res.getStatus() != 200)
		{
			throw new AuthenticationException("Could not login: " + res.getStatusInfo().getReasonPhrase());
		}

		token = response.get("access").getAsJsonObject().get("token").getAsJsonObject().get("id").getAsString();

		account =
				response.get("access").getAsJsonObject().get("token").getAsJsonObject().get("tenant").getAsJsonObject()
						.get("id").getAsString();
	}

	private void checkStatus()
	{
		if (token == null)
		{
			throw new IllegalStateException("Not logged in!");
		}
	}


	public ServerDetailsArray getServerDetails()
	{
		checkStatus();

		ServerDetailsArray serverDetailsArray = null;

		WebTarget target =
				client.target("https://servers.api.rackspacecloud.com").path("v1.0").path(account).path("servers")
						.path("detail");

		serverDetailsArray = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-Auth-Token", token).get(ServerDetailsArray.class);

		return serverDetailsArray;
	}
}
