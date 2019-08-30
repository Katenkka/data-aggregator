package net.ekatherine.code.aggregator.fetcher.controller;

import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Controller
@RequestMapping("/fetcher")
public abstract class MainController
{
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler({NoEntityFromExternalSourceFoundException.class})
	public void handleNotFound(final Exception ex)
	{
		//return Response.SC_BAD_REQUEST.
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler({IOException.class})
	public void handleIOException(final Exception ex) {

	}
}
