package net.ekatherine.code.aggregator.entity.tv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.StatusEnum;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.interfaces.HasExternalIdentifiers;
import net.ekatherine.code.aggregator.entity.interfaces.Series;
import net.ekatherine.code.aggregator.entity.interfaces.Timestampable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "tvshow")
public class TvShow implements Timestampable, Series<Episode>, HasExternalIdentifiers
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

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum status = StatusEnum.UNKNOWN;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tvshow_actor", joinColumns = @JoinColumn(name = "tvshow_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "party_id", referencedColumnName = "id"))
	private Set<Party> actors;

	@OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL)
	private List<Episode> episodes;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "premiered_at")
	private Instant premieredAt;

	@Column(name = "poster_url")
	private String posterUrl;

	@ElementCollection
	private Map<String, String> identifiers;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "tvshow_genre", joinColumns = @JoinColumn(name = "tvshow_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
	private Set<Subject> genres;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "updated_at")
	private Instant updatedAt;

	public TvShow()
	{
		actors = new HashSet<>();
		episodes = new ArrayList<>();
		identifiers = new HashMap<>();
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

	public void addEpisodes(final Set<Episode> episodes)
	{
		this.episodes.addAll(episodes);
	}

	public void updateEpisodes(final List<Episode> episodes)
	{
		final List<Episode> newEpisodes = new ArrayList<>();

		episodes.forEach(newEpisode ->
		{
			final Optional<Episode> found = this.episodes.stream().filter(episode -> episode.equals(newEpisode)).findFirst();
			if (found.isPresent()) {
				final Episode existingEpisode = found.get();
				existingEpisode.setAiredAt(newEpisode.getAiredAt());
				existingEpisode.setDescription(newEpisode.getDescription());
				existingEpisode.setTitle(newEpisode.getTitle());
				if (Objects.nonNull(existingEpisode.getTvShow()) && !existingEpisode.getTvShow().equals(this)) {
					existingEpisode.setTvShow(this);
				}
				newEpisodes.add(existingEpisode);
			} else {
				newEpisodes.add(newEpisode);
			}
		});

		this.episodes = newEpisodes;
	}

	public Instant getPremieredAt()
	{
		return premieredAt;
	}

	public void setPremieredAt(final Instant premieredAt)
	{
		this.premieredAt = premieredAt;
	}

	public StatusEnum getStatus()
	{
		return status;
	}

	public void setStatus(final StatusEnum status)
	{
		this.status = status;
	}

	@Override
	public void addIdentifier(final String key, final String val)
	{
		identifiers.put(key, val);
	}

	@Override
	public Map<String, String> getIdentifiers()
	{
		return identifiers;
	}

	@Override
	public void setIdentifiers(final Map<String, String> identifiers)
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

	@JsonIgnore
	@Override
	public Collection<Episode> getEntities()
	{
		return getEpisodes();
	}

	@Override
	public void setEntities(final Collection<Episode> entities)
	{
		setEpisodes((List<Episode>) entities);
	}

	public void setEntities(final List<Episode> entities)
	{
		setEpisodes(entities);
	}

	@JsonIgnore
	public List<Episode> getEpisodes()
	{
		return episodes;
	}

	public void setEpisodes(final List<Episode> episodes)
	{
		this.episodes = episodes;
	}

	public void addEpisode(final Episode episode)
	{
		episodes.add(episode);
	}

	@JsonInclude
	public Episode getLatestEpisode()
	{
		return episodes.isEmpty() ? null : episodes.get(episodes.size() - 1);
	}

	public String getPosterUrl()
	{
		return posterUrl;
	}

	public void setPosterUrl(final String posterUrl)
	{
		this.posterUrl = posterUrl;
	}
}