package net.ekatherine.code.aggregator.repository.interfaces;

import net.ekatherine.code.aggregator.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long>, FindByTitleEntityRepository<Subject>
{
}