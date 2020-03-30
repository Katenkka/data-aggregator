package net.ekatherine.code.aggregator.entity.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.ekatherine.code.aggregator.entity.interfaces.Series;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "book_series")
public class BookSeries implements Series<Book>
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

	@OneToMany(mappedBy = "bookSeries", cascade = CascadeType.ALL)
	private Set<Book> books;

	public BookSeries()
	{
		books = new HashSet<>();
	}

	@Override
	public Set<Book> getEntities()
	{
		return books;
	}

	@Override
	public void setEntities(final Collection<Book> books)
	{
		this.books = (Set<Book>) books;
	}
}
