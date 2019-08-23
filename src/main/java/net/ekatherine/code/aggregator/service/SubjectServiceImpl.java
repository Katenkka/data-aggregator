package net.ekatherine.code.aggregator.service;

import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.repository.interfaces.SubjectRepository;
import net.ekatherine.code.aggregator.service.interfaces.SubjectService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService
{
	private final SubjectRepository repository;

	public SubjectServiceImpl(final SubjectRepository repository)
	{
		this.repository = repository;
	}

	@Override
	public Subject getOne(final Long id)
	{
		return repository.getOne(id);
	}

	@Override
	public Subject save(final Subject entity)
	{
		//entity.setTitle(entity.getTitle().toLowerCase());
		final Optional<Subject> existing = repository.findOne(Example.of(entity));

		if (existing.isPresent())
		{
			Subject toUpdate = existing.get();
			toUpdate.setTitle(entity.getTitle().toLowerCase());
			return repository.saveAndFlush(toUpdate);
		}

		return repository.saveAndFlush(entity);
	}
}
