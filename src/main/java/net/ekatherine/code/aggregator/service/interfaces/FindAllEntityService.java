package net.ekatherine.code.aggregator.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

interface FindAllEntityService<T>
{
	List<T> findAll();
	Page<T> findAll(Pageable pageable);
}
