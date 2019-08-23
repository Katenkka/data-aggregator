package net.ekatherine.code.aggregator.fetcher.exception;

public class NoEntityFromExternalSourceFoundException extends Exception
{
	public NoEntityFromExternalSourceFoundException()
	{
	}

	public NoEntityFromExternalSourceFoundException(final String message)
	{
		super(message);
	}

	public NoEntityFromExternalSourceFoundException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public NoEntityFromExternalSourceFoundException(final Throwable cause)
	{
		super(cause);
	}

	public NoEntityFromExternalSourceFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
