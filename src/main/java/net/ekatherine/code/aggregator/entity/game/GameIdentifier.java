package net.ekatherine.code.aggregator.entity.game;

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
@Table(name = "game_identifier")
public class GameIdentifier implements ExternalIdentifier<Game>
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private Game game;

	@Column(name = "type", columnDefinition = "TEXT")
	@NotBlank
	private String type;

	@Column(name = "value", columnDefinition = "TEXT")
	@NotBlank
	private String value;

	public GameIdentifier()
	{
	}

	public GameIdentifier(@NotNull final Game game, @NotBlank final String type, @NotBlank final String value)
	{
		this.game = game;
		this.type = type;
		this.value = value;
	}

	@Override
	@JsonIgnore
	public Game getEntity()
	{
		return game;
	}

	@Override
	public void setEntity(final Game game)
	{
		this.game = game;
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
		final GameIdentifier that = (GameIdentifier) o;
		return game.equals(that.game) &&
			getType().equals(that.getType()) &&
			getValue().equals(that.getValue());
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public void setType(final String type)
	{
		this.type = type;
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
	public int hashCode()
	{
		return Objects.hash(game, getType(), getValue());
	}

	@Override
	public String toString()
	{
		return "GameIdentifier{" +
			"id=" + id +
			", game=" + game +
			", type='" + type + '\'' +
			", value='" + value + '\'' +
			'}';
	}
}
