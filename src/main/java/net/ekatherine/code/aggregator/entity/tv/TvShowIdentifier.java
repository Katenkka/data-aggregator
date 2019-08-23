package net.ekatherine.code.aggregator.entity.tv;

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
@Table(name = "tvshow_identifier")
public class TvShowIdentifier implements ExternalIdentifier<TvShow>
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "tvshow_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private TvShow tvShow;

	@Column(name = "type", columnDefinition = "TEXT")
	@NotBlank
	private String type;

	@Column(name = "value", columnDefinition = "TEXT")
	@NotBlank
	private String value;

	public TvShowIdentifier()
	{
	}

	public TvShowIdentifier(@NotNull final TvShow tvShow, @NotBlank final String type, @NotBlank final String value)
	{
		this.tvShow = tvShow;
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
		final TvShowIdentifier that = (TvShowIdentifier) o;
		return tvShow.equals(that.tvShow) &&
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
		return Objects.hash(tvShow, getType(), getValue());
	}

	@JsonIgnore
	@Override
	public TvShow getEntity()
	{
		return tvShow;
	}

	@Override
	public void setEntity(final TvShow tvShow)
	{
		this.tvShow = tvShow;
	}

	@Override
	public String toString()
	{
		return "TvShowIdentifier{" +
			"tvShow=" + tvShow +
			", type='" + type + '\'' +
			", value='" + value + '\'' +
			'}';
	}
}
