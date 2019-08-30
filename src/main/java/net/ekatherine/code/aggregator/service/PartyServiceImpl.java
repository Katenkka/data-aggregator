package net.ekatherine.code.aggregator.service;

import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.repository.interfaces.PartyRepository;
import net.ekatherine.code.aggregator.service.interfaces.PartyService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PartyServiceImpl implements PartyService
{
	private final PartyRepository partyRepository;

	public PartyServiceImpl(PartyRepository repository)
	{
		this.partyRepository = repository;
	}

	@Override
	public Party replaceWithExisting(Party obj)
	{
		final Optional<Party> existing = partyRepository.findOne(Example.of(obj));
		if (existing.isPresent())
		{
			obj = existing.get();
		}
		return obj;
	}
}
