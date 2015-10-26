package io.github.arqueue.hibernate.beans.helpers;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by root on 10/19/15.
 */
public class CountableList<E extends Countable> extends ArrayList<E>
{
	@Override
	public boolean add(E e)
	{
		if (e.getOrderNumber() == null)
		{
			e.setOrderNumber(this.size());
		}

		return super.add(e);
	}

	@Override
	public void add(int index, E element)
	{
		if (element.getOrderNumber() == null)
		{
			element.setOrderNumber(index);

			for (int i = index; i < size(); i++)
			{
				E e = get(i);

				if (e != null)
				{
					e.setOrderNumber(e.getOrderNumber() + 1);
				}
			}
		}

		super.add(index, element);
	}

	@Override
	public E set(int index, E element)
	{
		if (index < size())
		{
			if (get(index) != null)
			{
				element.setOrderNumber(get(index).getOrderNumber());
			}
		}
		else
		{
			element.setOrderNumber(size());
		}

		return super.set(index, element);
	}

	@Override
	public E remove(int index)
	{
		if (index >= 0 && index < size())
		{
			for (int i = index + 1; i < size(); i++)
			{
				E e = get(i);

				if (e != null)
				{
					e.setOrderNumber(e.getOrderNumber() - 1);
				}
			}
		}

		return super.remove(index);
	}

	@Override
	public boolean remove(Object o)
	{
		return super.remove(o);
	}

	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		int size = size();

		if (c != null)
		{
			for (E e : c)
			{
				if (e != null)
				{
					e.setOrderNumber(size);

					size++;
				}
			}
		}

		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		int idx = index;

		if (c != null)
		{
			for (E e : c)
			{
				if (e != null)
				{
					e.setOrderNumber(idx);

					idx++;
				}
			}

			int len = c.size();

			for (int i = index; i < size(); i++)
			{
				E e = get(i);

				if (e != null)
				{
					e.setOrderNumber(e.getOrderNumber() + len);
				}
			}
		}

		return super.addAll(index, c);
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex)
	{
		int diff = toIndex - fromIndex;

		if (toIndex < size() - 1 && fromIndex >= 0)
		{
			for (int i = toIndex + 1; i < size(); i++)
			{
				E e = get(i);

				if (e != null)
				{
					e.setOrderNumber(e.getOrderNumber() - diff);
				}
			}
		}

		super.removeRange(fromIndex, toIndex);
	}

}
