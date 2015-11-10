package io.github.arqueue.api.jcloud;

import io.github.arqueue.common.Configuration;
import io.github.arqueue.common.KeyValuePair;
import io.github.arqueue.exception.CacheException;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by root on 10/23/15.
 */
public class StaticDataCache
{
	public static final int DEFAULT_CAPACITY = 300;

	private static Logger logger = Logger.getLogger(StaticDataCache.class);

	private CacheNode first = null;

	private CacheNode last = null;

	private Map<String, CacheNode> cacheIndex = new ConcurrentHashMap<>();

	private Long lifetime;

	private int capacity;

	private static volatile StaticDataCache instance = null;

	ReentrantLock lock = new ReentrantLock(true);

	private StaticDataCache(int capacity, Long lifetime)
	{
		this.capacity = capacity;
		this.lifetime = lifetime;
	}

	public static StaticDataCache getInstance()
	{
		if (instance == null)
		{
			synchronized (StaticDataCache.class)
			{
				if (instance == null)
				{
					instance = new StaticDataCache(Configuration.getInstance().getCacheCapacity(),
							Configuration.getInstance().getCacheLifeTime());
				}
			}
		}

		return instance;
	}

	public int getCapacity()
	{
		return capacity;
	}

	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	public <T> void addToCache(String key, T object) throws CacheException
	{
		addToCache(key, object, this.lifetime);
	}

	public <T> void addToCache(String key, T object, Long lifetime) throws CacheException
	{
		lock.lock();

		try
		{
			if (!cacheIndex.containsKey(key))
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
						throw new CacheException("Cache consistency problem " + capacity);
					}
				}

				CacheNode<T> node = new CacheNode<>(key, object, lifetime);

				addNode(node);

				cacheIndex.put(key, node);
			}
			else
			{
				CacheNode<T> node = cacheIndex.get(key);

				if (node != null)
				{
					pop(node);

					node.setValue(object);

					node.setLifeTime(lifetime);

					cacheIndex.put(key, node);
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public <T> T getFromCache(String key)
	{
		lock.lock();

		try
		{
			if (cacheIndex.containsKey(key))
			{
				CacheNode<T> node = cacheIndex.get(key);

				if (node != null)
				{
					if (!node.isExpired())
					{
						pop(node);

						return node.getValue();
					}
					else
					{
						deleteNode(node);

						return null;
					}
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
		finally
		{
			lock.unlock();
		}
	}

	public void deleteFromCache(String key)
	{
		lock.lock();

		try
		{
			if (cacheIndex.containsKey(key))
			{
				CacheNode node = cacheIndex.get(key);

				if (node != null)
				{
					deleteNode(node);

					cacheIndex.remove(key);
				}
			}
		}
		finally
		{
			lock.unlock();
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

		private Long expirationTime;

		public CacheNode(String key, T value, CacheNode prev, CacheNode next, Long lifetime)
		{
			this.key = key;
			this.value = value;
			this.prev = prev;
			this.next = next;

			setLifeTime(lifetime);
		}

		public CacheNode(String key, T value)
		{
			this(key, value, null, null, null);
		}

		public CacheNode(String key, T value, Long lifetime)
		{
			this(key, value, null, null, lifetime);
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
			return new Date(expirationTime);
		}

		public void setExpirationDate(Date expirationDate)
		{
			this.expirationTime = expirationDate.getTime();
		}

		public boolean isExpired()
		{
			return (expirationTime != null && (new Date()).getTime() > expirationTime);
		}

		public Long getExpirationTime()
		{
			return expirationTime;
		}

		public void setExpirationTime(long expirationTime)
		{
			this.expirationTime = expirationTime;
		}

		public void setLifeTime(Long lifetime)
		{
			if (lifetime != null)
			{
				this.expirationTime = (new Date()).getTime() + lifetime;
			}
			else
			{
				this.expirationTime = null;
			}
		}
	}
}
