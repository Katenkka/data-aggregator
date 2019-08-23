package net.ekatherine.code.aggregator.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum StatusEnum
{
	UNKNOWN("unknown"),
	TBD("to be determined"),
	IN_DEVELOPMENT("in development"),
	RUNNING("running"),
	ON_AIR("on air"),
	HIATUS("hiatus"),
	ENDED("ended"),
	CLOSED("closed"),
	RELEASED("released"),
	TO_BE_RELEASED("to be released"),

	;


	private final String internalCode;

	StatusEnum(final String internalCode)
	{
		this.internalCode = internalCode;
	}

	@JsonCreator
	public static StatusEnum getStatusByInternalCode(final String code)
	{
		return Arrays.stream(StatusEnum.values())
			.filter(state -> state.getInternalCode().equalsIgnoreCase(code))
			.findFirst()
			.orElse(UNKNOWN);
	}

	@JsonValue
	public String getInternalCode()
	{
		return internalCode;
	}
}
