package net.ekatherine.code.aggregator.entity.interfaces;

public interface ExternalIdentifier<T>
{
	T getEntity();

	void setEntity(T entity);

	String getType();

	void setType(String type);

	String getValue();

	void setValue(String value);
}
