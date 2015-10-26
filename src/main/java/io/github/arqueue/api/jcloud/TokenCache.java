package io.github.arqueue.api.jcloud;

import io.github.arqueue.common.KeyValuePair;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/23/15.
 */
class TokenCache
{
	private static final int DEFAULT_CAPACITY = 300;

	private static final long DEFAULT_EXPIRATION_SECONDS = 24 * 60 * 60;

	private static Logger logger = Logger.getLogger(TokenCache.class);

	private CacheNode first = null;

	private CacheNode last = null;

	private Map<String, KeyValuePair<String, CacheNode>> cacheIndex = new HashMap<>();

	private long expirationSeconds;

	private int capacity;

	public TokenCache(int capacity)
	{
		this.capacity = capacity;
	}

	public TokenCache()
	{
		this(DEFAULT_CAPACITY);
	}

	public int getCapacity()
	{
		return capacity;
	}

	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	public void addToCache(String username, String password, String token) throws Exception
	{
		if (!cacheIndex.containsKey(username))
		{
			if (cacheIndex.size() == capacity)
			{
				if (last != null)
				{
					cacheIndex.remove(last.getKey());

					deleteNode(last);
				}
				else
				{
					throw new Exception("Cache consistency problem " + capacity);
				}
			}

			CacheNode<String> node = new CacheNode<>(username, token);

			addNode(node);

			cacheIndex.put(username, new KeyValuePair<>(password, node));
		}
		else
		{
			KeyValuePair<String, CacheNode> nodePair = cacheIndex.get(username);

			if (nodePair != null)
			{
				CacheNode<String> node = nodePair.getValue();

				if (node != null)
				{
					pop(node);

					node.setValue(token);

					cacheIndex.put(username, nodePair);
				}
			}
		}
	}

	public String getFromCache(String key)
	{
		if (cacheIndex.containsKey(key))
		{
			CacheNode<String> node = cacheIndex.get(key).getValue();

			if (node != null)
			{
				pop(node);

				return node.getValue();
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	public void deleteFromCache(String key)
	{
		if (cacheIndex.containsKey(key))
		{
			CacheNode<String> node = cacheIndex.get(key).getValue();

			if (node != null)
			{
				deleteNode(node);

				cacheIndex.remove(key);
			}
		}
	}

	private void addNode(CacheNode node)
	{
		if (node != null)
		{
			if (first != null)
			{
				node.setNext(first);

				node.setPrev(null);

				first.setPrev(node);

				first = node;
			}
			else
			{
				first = node;

				last = node;

				node.setNext(null);
				node.setPrev(null);
			}
		}
	}

	private void deleteNode(CacheNode node)
	{
		if (node != null)
		{
			if (node.prev != null)
			{
				node.prev.setNext(node.next);
			}
			else
			{
				first = node.next;
			}

			if (node.next != null)
			{
				node.next.setPrev(node.prev);
			}
			else
			{
				last = node.prev;
			}
		}
	}

	private void pop(CacheNode node)
	{
		if (node != null)
		{
			deleteNode(node);

			addNode(node);
		}
	}

	public void printCache()
	{
		logger.info("\n**** PRINTING CACHE *****");
		CacheNode node = first;

		while (node != null)
		{
			logger.info(node.getKey() + " : " + node.getValue());

			node = node.getNext();
		}
	}

	final class CacheNode<T>
	{
		private String key;

		private T value;

		private CacheNode prev;

		private CacheNode next;

		private Date expirationDate;

		public CacheNode(String key, T value, CacheNode prev, CacheNode next)
		{
			this.key = key;
			this.value = value;
			this.prev = prev;
			this.next = next;
		}

		public CacheNode(String key, T value)
		{
			this(key, value, null, null);
		}

		public T getValue()
		{
			return value;
		}

		public String getKey()
		{
			return key;
		}

		public void setKey(String key)
		{
			this.key = key;
		}

		public void setValue(T value)
		{
			this.value = value;
		}

		public CacheNode getPrev()
		{
			return prev;
		}

		public void setPrev(CacheNode prev)
		{
			this.prev = prev;
		}

		public CacheNode getNext()
		{
			return next;
		}

		public void setNext(CacheNode next)
		{
			this.next = next;
		}

		public Date getExpirationDate()
		{
			return expirationDate;
		}

		public void setExpirationDate(Date expirationDate)
		{
			this.expirationDate = expirationDate;
		}

		public boolean isExpired(Date date)
		{
			return date.getTime() > expirationDate.getTime();
		}
	}
}
