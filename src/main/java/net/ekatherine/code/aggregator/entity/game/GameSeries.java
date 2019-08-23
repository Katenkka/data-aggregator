package net.ekatherine.code.aggregator.entity.game;

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
@Table(name = "game_series")
public class GameSeries implements Series<Game>
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

	@OneToMany(mappedBy = "gameSeries", cascade = CascadeType.ALL)
	private Set<Game> games;

	public GameSeries()
	{
		games = new HashSet<>();
	}

	@Override
	public Collection<Game> getEntities()
	{
		return games;
	}

	@Override
	public void setEntities(final Collection<Game> games)
	{
		this.games = (Set<Game>) games;
	}
}
