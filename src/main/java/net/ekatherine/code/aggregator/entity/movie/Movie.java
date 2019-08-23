package net.ekatherine.code.aggregator.entity.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.StatusEnum;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.interfaces.Timestampable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "movie")
public class Movie implements Timestampable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "title", nullable = false)
	@NotBlank
	private String title;

	@Column(name = "description", length = 1000)
	private String description;

	@Column(name = "metascore")
	private Double metascore;

	@Column(name = "imdb_rating")
	private Double imdbRating;

	@Column(name = "poster_url")
	private String posterUrl;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum status = StatusEnum.UNKNOWN;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_director", joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "party_id", referencedColumnName = "id"))
	private Set<Party> directors;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_actor", joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "party_id", referencedColumnName = "id"))
	private Set<Party> actors;

	@ManyToOne
	@JoinColumn
	private MovieSeries movieSeries;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "released_at")
	private Instant releasedAt;

	@OneToMany(mappedBy = "movie")
	private Set<MovieIdentifier> identifiers;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
	private Set<Subject> genres;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "updated_at")
	private Instant updatedAt;

	public Movie()
	{
		actors = new HashSet<>();
		directors = new HashSet<>();
		identifiers = new HashSet<>();
		genres = new HashSet<>();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public MovieSeries getMovieSeries()
	{
		return movieSeries;
	}

	public void setMovieSeries(final MovieSeries movieSeries)
	{
		this.movieSeries = movieSeries;
	}

	public Set<Party> getDirectors()
	{
		return directors;
	}

	public void setDirectors(final Set<Party> directors)
	{
		this.directors = directors;
	}

	public void addDirector(final Party director)
	{
		directors.add(director);
	}

	public Double getMetascore()
	{
		return metascore;
	}

	public void setMetascore(final Double metascore)
	{
		this.metascore = metascore;
	}

	public Double getImdbRating()
	{
		return imdbRating;
	}

	public void setImdbRating(final Double imdbRating)
	{
		this.imdbRating = imdbRating;
	}

	public String getPosterUrl()
	{
		return posterUrl;
	}

	public void setPosterUrl(final String posterUrl)
	{
		this.posterUrl = posterUrl;
	}

	public StatusEnum getStatus()
	{
		return status;
	}

	public void setStatus(final StatusEnum status)
	{
		this.status = status;
	}

	public Set<Party> getActors()
	{
		return actors;
	}

	public void setActors(final Set<Party> actors)
	{
		this.actors = actors;
	}

	public void addActor(final Party actor)
	{
		actors.add(actor);
	}

	public Instant getReleasedAt()
	{
		return releasedAt;
	}

	public void setReleasedAt(final Instant releasedAt)
	{
		this.releasedAt = releasedAt;
	}

	public Set<MovieIdentifier> getIdentifiers()
	{
		return identifiers;
	}

	public void setIdentifiers(final Set<MovieIdentifier> identifiers)
	{
		this.identifiers = identifiers;
	}

	public Set<Subject> getGenres()
	{
		return genres;
	}

	public void setGenres(final Set<Subject> genres)
	{
		this.genres = genres;
	}

	public void addGenre(final Subject genre)
	{
		genres.add(genre);
	}

	@Override
	public Instant getCreatedAt()
	{
		return createdAt;
	}

	@Override
	public void setCreatedAt(final Instant createdAt)
	{
		this.createdAt = createdAt;
	}

	@Override
	public Instant getUpdatedAt()
	{
		return updatedAt;
	}

	@Override
	public void setUpdatedAt(final Instant updatedAt)
	{
		this.updatedAt = updatedAt;
	}
}
