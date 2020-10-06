package net.ekatherine.code.aggregator.service;

import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.repository.interfaces.PartyRepository;
import net.ekatherine.code.aggregator.service.interfaces.PartyService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService
{
	private final PartyRepository partyRepository;

	@Override
	public Party replaceWithExisting(Party obj)
	{
		final List<Party> parties = partyRepository.findByTitleIgnoreCase(obj.getTitle());

		if (!parties.isEmpty())
		{
			return parties.get(0);
		}

		return obj;
	}
}
