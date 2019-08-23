package net.ekatherine.code.aggregator.entity.game;

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
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "game")
public class Game implements Timestampable
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

	@Column(name = "aliases", length = 1000)
	private String aliases;

	@Column(name = "metascore")
	private Double metascore;

	@Column(name = "poster_url")
	private String posterUrl;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum status = StatusEnum.UNKNOWN;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "game_developer", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "party_id", referencedColumnName = "id"))
	private Set<Party> developers;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "game_concept", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
	private Set<Subject> concepts;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "game_genre", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
	private Set<Subject> genres;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "game_theme", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
	private Set<Subject> themes;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "game_publisher", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "party_id", referencedColumnName = "id"))
	private Set<Party> publishers;

	@OneToMany(mappedBy = "game")
	private Set<GameIdentifier> identifiers;

	@ManyToOne
	@JoinColumn
	private GameSeries gameSeries;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "released_at")
	private Instant releasedAt;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "updated_at")
	private Instant updatedAt;

	public Game()
	{
		developers = new HashSet<>();
		concepts = new HashSet<>();
		genres = new HashSet<>();
		themes = new HashSet<>();
		publishers = new HashSet<>();
		identifiers = new HashSet<>();
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final Game game = (Game) o;
		return getTitle().equals(game.getTitle()) &&
			Objects.equals(getReleasedAt(), game.getReleasedAt());
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public Instant getReleasedAt()
	{
		return releasedAt;
	}

	public void setReleasedAt(final Instant releasedAt)
	{
		this.releasedAt = releasedAt;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getTitle(), getReleasedAt());
	}

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public String getAliases()
	{
		return aliases;
	}

	public void setAliases(final String aliases)
	{
		this.aliases = aliases;
	}

	public Double getMetascore()
	{
		return metascore;
	}

	public void setMetascore(final Double metascore)
	{
		this.metascore = metascore;
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

	public Set<Party> getDevelopers()
	{
		return developers;
	}

	public void setDevelopers(final Set<Party> developers)
	{
		this.developers = developers;
	}

	public void addDeveloper(final Party developer)
	{
		developers.add(developer);
	}

	public Set<Subject> getConcepts()
	{
		return concepts;
	}

	public void setConcepts(final Set<Subject> concepts)
	{
		this.concepts = concepts;
	}

	public void addConcept(final Subject concept)
	{
		concepts.add(concept);
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

	public Set<Subject> getThemes()
	{
		return themes;
	}

	public void setThemes(final Set<Subject> themes)
	{
		this.themes = themes;
	}

	public void addTheme(final Subject theme)
	{
		themes.add(theme);
	}

	public Set<Party> getPublishers()
	{
		return publishers;
	}

	public void setPublishers(final Set<Party> publishers)
	{
		this.publishers = publishers;
	}

	public void addPublisher(final Party publisher)
	{
		publishers.add(publisher);
	}

	public Set<GameIdentifier> getIdentifiers()
	{
		return identifiers;
	}

	public void setIdentifiers(final Set<GameIdentifier> identifiers)
	{
		this.identifiers = identifiers;
	}

	public void addIdentifier(final GameIdentifier identifier)
	{
		identifiers.add(identifier); 
	}

	public GameSeries getGameSeries()
	{
		return gameSeries;
	}

	public void setGameSeries(final GameSeries gameSeries)
	{
		this.gameSeries = gameSeries;
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