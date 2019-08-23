package net.ekatherine.code.aggregator.entity.tv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "tvshow_episode")
public class Episode
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn
	private TvShow tvShow;

	@Column(name = "title", nullable = false)
	@NotBlank
	private String title = "UNKNOWN";

	@Column(name = "number", nullable = false)
	@NonNull
	private Long number;

	@Column(name = "season", nullable = false)
	@NonNull
	private Long season;

	@Column(name = "poster_url")
	private String posterUrl;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "aired_at")
	private Instant airedAt;

	@Column(name = "description", length = 1000)
	private String description;

	public Episode()
	{
	}

	public Episode(final TvShow tvShow, @NotBlank final String title, final Long number, final Long season, final Instant airedAt)
	{
		this.tvShow = tvShow;
		this.title = title;
		this.number = number;
		this.season = season;
		this.airedAt = airedAt;
	}

	public static int comparator(final Episode episode1, final Episode episode2)
	{
		return episode1.getSeason().compareTo(episode2.getSeason()) * 100 + episode1.getNumber().compareTo(episode2.getNumber());
	}

	@JsonIgnore
	public Long getSeason()
	{
		return season;
	}

	@JsonIgnore
	public Long getNumber()
	{
		return number;
	}

	public void setNumber(final Long number)
	{
		this.number = number;
	}

	public void setSeason(final Long season)
	{
		this.season = season;
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
		final Episode episode = (Episode) o;
		return getTvShow().equals(episode.getTvShow()) &&
			getTitle().equals(episode.getTitle()) &&
			getNumber().equals(episode.getNumber()) &&
			getSeason().equals(episode.getSeason()) &&
			Objects.equals(getPosterUrl(), episode.getPosterUrl()) &&
			getAiredAt().equals(episode.getAiredAt()) &&
			Objects.equals(getDescription(), episode.getDescription());
	}

	public TvShow getTvShow()
	{
		return tvShow;
	}

	public void setTvShow(final TvShow tvShow)
	{
		this.tvShow = tvShow;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public String getPosterUrl()
	{
		return posterUrl;
	}

	public Instant getAiredAt()
	{
		return airedAt;
	}

	public void setAiredAt(final Instant airedAt)
	{
		this.airedAt = airedAt;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public void setPosterUrl(final String posterUrl)
	{
		this.posterUrl = posterUrl;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getTvShow(), getTitle(), getNumber(), getSeason(), getPosterUrl(), getAiredAt(), getDescription());
	}

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public String getFullNumber()
	{
		return String.format("%dx%02d", season, number);
	}

	@Override
	public String toString()
	{
		return "Episode{" +
			"id=" + id +
			", tvShow=" + tvShow +
			", title='" + title + '\'' +
			", number=" + number +
			", season=" + season +
			", posterUrl='" + posterUrl + '\'' +
			", airedAt=" + airedAt +
			", description='" + description + '\'' +
			'}';
	}
}
