package net.ekatherine.code.aggregator.fetcher.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class FetcherUtil
{
	public String connectAndRetrieveJson(final String url, final int timeout) throws IOException
	{
		HttpURLConnection c = null;
		try
		{
			final URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			c.setRequestProperty("Content-length", "0");
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.setConnectTimeout(timeout);
			c.setReadTimeout(timeout);
			c.connect();
			final int status = c.getResponseCode();

			switch (status)
			{
				case 200:
				case 201:
					final BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
					final StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null)
					{
						sb.append(line).append("\n");
					}
					br.close();
					return sb.toString();

				default:
					throw new IOException("Received status: " + status);
			}
		} finally {
			if (c != null)
			{
				try
				{
					c.disconnect();
				} catch (final Exception ex)
				{
					log.warn("An error occurred", ex);
				}
			}
		}
	}
}