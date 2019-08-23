package net.ekatherine.code.aggregator.repository.interfaces;

import net.ekatherine.code.aggregator.entity.tv.TvShow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TvShowRepository extends JpaRepository<TvShow, Long>
{
}
