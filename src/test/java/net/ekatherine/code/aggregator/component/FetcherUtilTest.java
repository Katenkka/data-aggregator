package net.ekatherine.code.aggregator.component;

import net.ekatherine.code.aggregator.fetcher.FetcherUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FetcherUtilTest
{
	private FetcherUtil fetcherUtil = new FetcherUtil();

	@Test
	public void escapeQuotesSuccessfully()
	{
		final String given = "\"The second Veronica Mars novel, in which she investigates a mysterious crime at The Neptune Grand hotel\" --";

		final String sanitized = new Util().sanitize(given);

		final String expected = "\"The second Veronica Mars novel, in which she investigates a mysterious crime at The Neptune Grand hotel\" --";

		Assert.assertEquals(expected, sanitized);
	}

	@Test
	public void removeHtmlSuccessfully()
	{
		final String given = "<b>The second Veronica Mars novel, in which <i>she investigates</i> a mysterious crime at The Neptune Grand hotel</b>";

		final String sanitized = new Util().sanitize(given);

		final String expected = "The second Veronica Mars novel, in which she investigates a mysterious crime at The Neptune Grand hotel";

		Assert.assertEquals(expected, sanitized);
	}

	@Test
	public void replaceCharactersSuccessfully()
	{
		final String given = "          previous Péndulo Studios game                 ";

		final String sanitized = new Util().sanitize(given);

		final String expected = "previous Péndulo Studios game";

		Assert.assertEquals(expected, sanitized);
	}

	@Test
	public void removeWWhitespacesSuccessfully()
	{
		final String given = "             <b>The second Veronica Mars novel, in which <i>she investigates</i> a mysterious crime at The Neptune Grand hotel</b>                  ";

		final String sanitized = new Util().sanitize(given);

		final String expected = "The second Veronica Mars novel, in which she investigates a mysterious crime at The Neptune Grand hotel";

		Assert.assertEquals(expected, sanitized);
	}
}