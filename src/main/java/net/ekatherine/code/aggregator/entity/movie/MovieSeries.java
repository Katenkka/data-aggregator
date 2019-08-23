package net.ekatherine.code.aggregator.entity.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.ekatherine.code.aggregator.entity.interfaces.Series;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "movie_series")
public class MovieSeries implements Series<Movie>
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "title", nullable = false)
	@NotBlank
	private String title;

	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "movieSeries", cascade = CascadeType.ALL)
	private Set<Movie> movies;

	public MovieSeries()
	{
		movies = new HashSet<>();
	}

	@Override
	public Set<Movie> getEntities()
	{
		return movies;
	}

	@Override
	public void setEntities(final Collection<Movie> movies)
	{
		this.movies = (Set<Movie>) movies;
	}
}
