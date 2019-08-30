package net.ekatherine.code.aggregator.entity.interfaces;

import java.util.Map;

public interface HasExternalIdentifiers
{
	void addIdentifier(String key, String val);

	Map<String, String> getIdentifiers();

	void setIdentifiers(Map<String, String> identifiers);
}
