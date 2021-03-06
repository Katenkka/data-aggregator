package net.ekatherine.code.aggregator.service;

import lombok.RequiredArgsConstructor;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.repository.interfaces.SubjectRepository;
import net.ekatherine.code.aggregator.service.interfaces.SubjectService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService
{
	private final SubjectRepository subjectRepository;

	public Subject replaceWithExisting(Subject subject)
	{
		final List<Subject> subjects = subjectRepository.findByTitleIgnoreCase(subject.getTitle());
		if (!subjects.isEmpty())
		{
			return subjects.get(0);
		}
		return subject;
	}
}
