package net.ekatherine.code.aggregator.entity.book;

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
@Table(name = "book_identifier")
public class BookIdentifier implements ExternalIdentifier<Book>
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
	@NotNull
	private Book book;

	@Column(name = "type", columnDefinition = "TEXT")
	@NotBlank
	private String type;

	@Column(name = "value", columnDefinition = "TEXT")
	@NotBlank
	private String value;

	public BookIdentifier()
	{
	}

	public BookIdentifier(@NotNull final Book book, @NotBlank final String type, @NotBlank final String value)
	{
		this.book = book;
		this.type = type;
		this.value = value;
	}

	@Override
	@JsonIgnore
	public Book getEntity()
	{
		return book;
	}

	@Override
	public void setEntity(final Book book)
	{
		this.book = book;
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
		final BookIdentifier that = (BookIdentifier) o;
		return book.equals(that.book) &&
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
		return Objects.hash(book, getType(), getValue());
	}

	@Override
	public String toString()
	{
		return "BookIdentifier{" +
			"id=" + id +
			", book=" + book +
			", type='" + type + '\'' +
			", value='" + value + '\'' +
			'}';
	}
}
