package net.ekatherine.code.aggregator.entity.interfaces;

import java.time.Instant;

public interface Timestampable
{
    Instant getCreatedAt();

	void setCreatedAt(Instant createdAt);

    Instant getUpdatedAt();

	void setUpdatedAt(Instant updatedAt);
}
