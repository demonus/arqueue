package io.github.arqueue.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 10/16/15.
 */
public class Properties extends java.util.Properties
{
	private Map<String, Integer> keysIndex;

	private List<String> values;

	Pattern ptKeyValue;

	private String filename;

	private Properties(String filename)
	{
		this();

		this.filename = filename;
	}

	public Properties()
	{
		super();

		keysIndex = new HashMap<String, Integer>();

		values = new ArrayList<String>();

		ptKeyValue = Pattern.compile("(^ *)([^#!= ]+)( *= *)(.*)( *)");
	}

	public static Properties load(String filename) throws IOException
	{
		Properties properties = new Properties(filename);

		properties.loadFromFile();

		return properties;
	}


	public void loadFromFile() throws IOException
	{
		loadFromFile(filename);
	}

	public void loadFromFile(String filename) throws IOException
	{
		BufferedReader brProps = null;

		String line;

		try
		{
			if (filename == null)
			{
				throw new IOException("File name is null");
			}

			File file = new File(filename);

			if (!file.exists() || !file.canRead())
			{
				throw new IOException("File " + filename + " is missing or not accessible");
			}

			FileReader fr = new FileReader(file);

			brProps = new BufferedReader(fr);

			this.filename = filename;

			while ((line = brProps.readLine()) != null)
			{
				values.add(line);

				int index = values.size() - 1;

				Matcher m = ptKeyValue.matcher(line);

				if (m.find())
				{
					keysIndex.put(m.group(2), index);

					super.setProperty(m.group(2), m.group(4));
				}
			}
		}
		finally
		{
			Utils.closeResources(brProps);
		}
	}

	public void save() throws IOException
	{
		saveAs(filename);
	}

	public void saveAs(String saveFileName) throws IOException
	{
		PrintWriter pwSave = null;

		if (filename != null)
		{
			try
			{
				Set<Object> keys = keySet();

				for (Object key : keys)
				{
					Integer index = keysIndex.get(key);

					if (index != null && index < values.size())
					{
						String line = values.get(index);

						Matcher mmReplace = ptKeyValue.matcher(line);

						StringBuffer sb = new StringBuffer();

						while (mmReplace.find())
						{
							String text = mmReplace.group(1) + mmReplace.group(2) + mmReplace.group(3) +
									getProperty(key.toString()) + mmReplace.group(5);

							mmReplace.appendReplacement(sb, text);
						}

						mmReplace.appendTail(sb);

						values.set(index, sb.toString());
					}
				}

				File file = new File(filename);

				if ((file.exists() && !file.canWrite()) || (!file.exists() && !file.createNewFile()))
				{
					throw new IOException("No write/create permission for file " + filename);
				}

				FileWriter fw = new FileWriter(saveFileName);

				pwSave = new PrintWriter(fw);

				for (String line : values)
				{
					pwSave.println(line);
				}
			}
			finally
			{
				Utils.closeResources(pwSave);
			}
		}
		else
		{
			throw new IOException("File name is null");
		}
	}

	@Override
	public synchronized Object setProperty(String key, String value)
	{
		if (!keySet().contains(key))
		{
			values.add(key + "=" + value);

			keysIndex.put(key, values.size() - 1);
		}

		return super.setProperty(key, value);
	}
}
