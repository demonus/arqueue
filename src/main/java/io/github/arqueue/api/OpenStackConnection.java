package io.github.arqueue.api;

import com.google.gson.JsonObject;
import io.github.arqueue.api.beans.get.flavor.Flavors;
import io.github.arqueue.api.beans.get.image.Image;
import io.github.arqueue.api.beans.get.image.Images;
import io.github.arqueue.api.beans.get.login.Endpoint;
import io.github.arqueue.api.beans.get.login.LoginResponse;
import io.github.arqueue.api.beans.get.login.Service;
import io.github.arqueue.api.beans.get.server.ServerDetailsArrayResponse;
import io.github.arqueue.api.beans.post.request.server.ServerRequest;
import io.github.arqueue.api.post.response.server.ServerResponse;
import io.github.arqueue.common.Configuration;
import io.github.arqueue.common.RequestBuilder;
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
 * Jax-RS based REST client for OpenStack.
 * Developed to be used with Rackspace Public Cloud.
 * <p>
 * Uses GSon to marshall/unmarshall JSON beans.
 * <p>
 *
 * @author Dmitriy Bannikov
 */
public class OpenStackConnection
{
	public static final String ALIAS_SERVER = "cloudServersOpenStack";

	private ClientConfig clientConfig;

	private Client client;

	private String token = null;

	private String account = null;

	private String defaultRegion;

	private Map<String, Map<String, Endpoint>> targets = new HashMap<>();

	public enum HttpMethod
	{
		GET,
		POST,
		DELETE,
		PUT;
	}

	public OpenStackConnection()
	{
		clientConfig = new ClientConfig();

		client = ClientBuilder.newClient(clientConfig);
	}

	/**
	 * This method should be called first to login, and get an access token and endpoint URLs
	 *
	 * @param username Rackspace Public Cloud username
	 * @param apiKey   currently, only Rackspace Public Cloud API Key is supported. No passwords.
	 * @throws AuthenticationException authentication failed due to invalid credentials or a connectivity problem
	 */
	public void login(String username, String apiKey) throws AuthenticationException
	{
		JsonObject keyCredentials = new JsonObject();

		keyCredentials.addProperty("username", username);
		keyCredentials.addProperty("apiKey", apiKey);

		JsonObject auth = new JsonObject();

		auth.add("RAX-KSKEY:apiKeyCredentials", keyCredentials);

		JsonObject login = new JsonObject();

		login.add("auth", auth);

		/*Properties file server.conf should contain a key openstack.identity-url pointing to the identity URL.
		* By default Rackspace Identity URL will be used - https://identity.api.rackspacecloud.com/v2.0/tokens*/
		WebTarget target = client.target(Configuration.getInstance().getOpenStackIdentityUrl());

		Response res = target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(login.toString()));

		if (res.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL)
		{
			throw new AuthenticationException("Could not login: " + res.getStatusInfo().getReasonPhrase());
		}

		LoginResponse loginResponse = Utils.parse(res.readEntity(String.class), LoginResponse.class);

		token = loginResponse.getAccess().getToken().getId();

		account = loginResponse.getAccess().getToken().getTenant().getId();

		defaultRegion = loginResponse.getAccess().getUser().getDefaultRegion();

		/*Converting the list of received endpoints into a Map*/
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

	/**
	 * Should be called to verify that login operation was completed successfully.
	 */
	private void checkStatus()
	{
		if (token == null)
		{
			throw new IllegalStateException("Not logged in!");
		}
	}

	/**
	 * Submit a request to create a new OpenStack server in default region
	 *
	 * @param serverRequest bean class for a new OpenStack server JSON request
	 * @return response an object which contains new server's ID and direct REST URL
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public ServerResponse createServer(ServerRequest serverRequest)
			throws OpenStackApiException, ValidationException
	{
		return createServer(serverRequest, defaultRegion);
	}

	/**
	 * Submit a request to create a new OpenStack server
	 *
	 * @param serverRequest bean class for a new OpenStack server JSON request
	 * @param region        endpoint region
	 * @return response object that contains new server's ID and direct REST URL
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public ServerResponse createServer(ServerRequest serverRequest, String region)
			throws OpenStackApiException, ValidationException
	{
		JsonObject jsonObject = new JsonObject();

		jsonObject.add("server", Utils.toJsonElement(serverRequest));

		return submitRequest(ServerResponse.class,
				(new RequestBuilder(region)).entity(jsonObject.toString()).alias(OpenStackConnection.ALIAS_SERVER)
						.method(HttpMethod.POST).path("servers"));
	}

	/**
	 * Get a detailed list of Cloud servers for defaul region
	 *
	 * @return a class which contains a list of existing servers
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public ServerDetailsArrayResponse getServerDetails() throws OpenStackApiException, ValidationException
	{
		return getServerDetails(defaultRegion);
	}

	/**
	 * Get a detailed list of Cloud servers
	 *
	 * @param region endpoint region
	 * @return an object which contains a list of existing servers
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public ServerDetailsArrayResponse getServerDetails(String region) throws OpenStackApiException, ValidationException
	{
		checkStatus();

		ServerDetailsArrayResponse serverDetailsArrayResponse =
				submitRequest(ServerDetailsArrayResponse.class,
						(new RequestBuilder(region)).alias(OpenStackConnection.ALIAS_SERVER).path("servers")
								.path("detail"));

		return serverDetailsArrayResponse;
	}

	/**
	 * Get a list of hardware flavors for default region
	 *
	 * @return an object which contains a list of flavors
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public Flavors listFlavors() throws OpenStackApiException, ValidationException
	{
		return listFlavors(null);
	}

	/**
	 * Get a list of hardware flavors
	 *
	 * @param region endpoint region
	 * @return an object which contains a list of flavors
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public Flavors listFlavors(String region) throws OpenStackApiException, ValidationException
	{
		Flavors flavors =
				submitRequest(Flavors.class,
						(new RequestBuilder(region)).alias(OpenStackConnection.ALIAS_SERVER).path("flavors"));

		return flavors;
	}

	/**
	 * Get and endpoint URL
	 *
	 * @param requestBuilder request parameters
	 * @return a public URL of the endpoint
	 * @throws ValidationException endpoint not found
	 */
	private String getUrl(RequestBuilder requestBuilder) throws ValidationException
	{
		if (targets.containsKey(requestBuilder.getAlias()))
		{
			Map<String, Endpoint> links = targets.get(requestBuilder.getAlias());

			if (links.containsKey(requestBuilder.getRegion()))
			{
				return links.get(requestBuilder.getRegion()).getPublicURL();
			}
		}

		throw new ValidationException(
				"Endpoint public URL not found for given request: " + requestBuilder);
	}


	/**
	 * Submit a REST request, gets the result, and maps it into a Java class
	 *
	 * @param clazz          return class name
	 * @param requestBuilder request parameters
	 * @param <T>            return type
	 * @return an instance of T
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	private <T> T submitRequest(Class<T> clazz, RequestBuilder requestBuilder)
			throws OpenStackApiException, ValidationException
	{
		Invocation.Builder request = getRequest(requestBuilder);

		Response response;

		if (requestBuilder.isEntityBased())
		{
			response = request.post(Entity.json(requestBuilder.getEntity()));
		}
		else if (Utils.in(requestBuilder.getMethod(), HttpMethod.POST, HttpMethod.PUT))
		{
			Form form = new Form();

			for (Map.Entry<String, String> entry : requestBuilder.getParameters().entrySet())
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

	/**
	 * Get a list of saved server images for default region
	 *
	 * @return an object which contains a list of saved images
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public Images listImages() throws OpenStackApiException, ValidationException
	{
		return listImages(null);
	}

	/**
	 * Get a list of saved snapshot (custom) images for default region
	 *
	 * @return an object which contains a list of saved snapshot images
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public Images listSnapshotImages() throws OpenStackApiException, ValidationException
	{
		return listSnapshotImages(null);
	}

	/**
	 * Get a list of saved snapshot (custom) images
	 *
	 * @param region endpoint region
	 * @return an object which contains a list of saved snapshot images
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
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

	/**
	 * Get a list of saved images
	 *
	 * @param region endpoint region
	 * @return an object which contains a list of saved images
	 * @throws OpenStackApiException communication failed
	 * @throws ValidationException   endpoint not found
	 */
	public Images listImages(String region) throws OpenStackApiException, ValidationException
	{
		return submitRequest(Images.class, (new RequestBuilder(region)).alias(OpenStackConnection.ALIAS_SERVER).path
				("images").path("detail"));
	}

	/**
	 * Prepare a REST request, gets the result, and maps it into a Java class
	 *
	 * @param requestBuilder request parameters
	 * @return Jax-RS request builder
	 * @throws ValidationException endpoint not found
	 */
	private Invocation.Builder getRequest(RequestBuilder requestBuilder) throws ValidationException
	{
		checkStatus();

		if (requestBuilder.getRegion() == null)
		{
			requestBuilder.region(defaultRegion);
		}

		String url = getUrl(requestBuilder);

		WebTarget target = client.target(url);

		for (String uriPathElement : requestBuilder.getPaths())
		{
			target = target.path(uriPathElement);
		}

		Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-Auth-Token", token);

		for (Map.Entry<String, String> header : requestBuilder.getHeaders().entrySet())
		{
			request.header(header.getKey(), header.getValue());
		}

		if (!requestBuilder.isEntityBased())
		{
			for (Map.Entry<String, String> param : requestBuilder.getParameters().entrySet())
			{
				request.property(param.getKey(), param.getValue());
			}
		}

		return request;
	}

	/**
	 * Get default region
	 *
	 * @return default region
	 */
	public String getDefaultRegion()
	{
		return defaultRegion;
	}

	/**
	 * Set default region
	 *
	 * @param defaultRegion default region
	 */
	public void setDefaultRegion(String defaultRegion)
	{
		this.defaultRegion = defaultRegion;
	}
}
