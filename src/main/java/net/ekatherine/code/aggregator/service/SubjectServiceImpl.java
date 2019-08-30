package net.ekatherine.code.aggregator.service;

import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.repository.interfaces.SubjectRepository;
import net.ekatherine.code.aggregator.service.interfaces.SubjectService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubjectServiceImpl implements SubjectService
{
	private final SubjectRepository subjectRepository;

	public SubjectServiceImpl(SubjectRepository subjectRepository)
	{
		this.subjectRepository = subjectRepository;
	}

	public Subject replaceWithExisting(Subject category)
	{
		final List<Subject> existing = subjectRepository.findAll(Example.of(category));
		if (!existing.isEmpty())
		{
			category = existing.iterator().next();
		}
		return category;
	}
}
