package net.ekatherine.code.aggregator.entity.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.ekatherine.code.aggregator.entity.interfaces.ExternalIdentifier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "movie_identifier")
public class MovieIdentifier implements ExternalIdentifier<Movie>
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private Movie movie;

	@Column(name = "type", columnDefinition = "TEXT")
	@NotBlank
	private String type;

	@Column(name = "value", columnDefinition = "TEXT")
	@NotBlank
	private String value;

	public MovieIdentifier()
	{
	}

	public MovieIdentifier(@NotNull final Movie movie, @NotBlank final String type, @NotBlank final String value)
	{
		this.movie = movie;
		this.type = type;
		this.value = value;
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
		final MovieIdentifier that = (MovieIdentifier) o;
		return movie.equals(that.movie) &&
			getType().equals(that.getType()) &&
			getValue().equals(that.getValue());
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(final String value)
	{
		this.value = value;
	}

	@Override
	public void setType(final String type)
	{
		this.type = type;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(movie, getType(), getValue());
	}

	@Override
	@JsonIgnore
	public Movie getEntity()
	{
		return movie;
	}

	@Override
	public void setEntity(final Movie movie)
	{
		this.movie = movie;
	}

}
