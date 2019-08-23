package net.ekatherine.code.aggregator.repository.interfaces;

import net.ekatherine.code.aggregator.entity.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long>
{
}
