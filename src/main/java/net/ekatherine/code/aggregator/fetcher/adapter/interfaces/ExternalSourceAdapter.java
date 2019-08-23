package net.ekatherine.code.aggregator.fetcher.adapter.interfaces;

import net.ekatherine.code.aggregator.fetcher.exception.NoEntityFromExternalSourceFoundException;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

public interface ExternalSourceAdapter<T>
{
	T getEntity(@NotBlank String mainKey) throws IOException, NoEntityFromExternalSourceFoundException;
}
