package net.ekatherine.code.aggregator.entity.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.ekatherine.code.aggregator.entity.Party;
import net.ekatherine.code.aggregator.entity.StatusEnum;
import net.ekatherine.code.aggregator.entity.Subject;
import net.ekatherine.code.aggregator.entity.interfaces.HasExternalIdentifiers;
import net.ekatherine.code.aggregator.entity.interfaces.Timestampable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "book")
public class Book implements Timestampable, HasExternalIdentifiers
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

	@Column(name = "cover_url")
	private String coverUrl;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum status = StatusEnum.UNKNOWN;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "party_id", referencedColumnName = "id"))
	private Set<Party> authors;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
	private Set<Subject> categories;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "book_publisher", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "party_id", referencedColumnName = "id"))
	private Set<Party> publishers;

	@ElementCollection
	private Map<String, String> identifiers;

	@ManyToOne
	@JoinColumn
	private BookSeries bookSeries;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "published_at")
	private Instant publishedAt;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name = "updated_at")
	private Instant updatedAt;

	public Book()
	{
		authors = new HashSet<>();
		categories = new HashSet<>();
		publishers = new HashSet<>();
		identifiers = new HashMap<>();
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

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public StatusEnum getStatus()
	{
		return status;
	}

	public void setStatus(final StatusEnum status)
	{
		this.status = status;
	}

	public void addAuthors(final Set<Party> authors)
	{
		this.authors.addAll(authors);
	}

	public void addAuthor(final Party author)
	{
		authors.add(author);
	}

	public BookSeries getBookSeries()
	{
		return bookSeries;
	}

	public void setBookSeries(final BookSeries bookSeries)
	{
		this.bookSeries = bookSeries;
	}

	public void addCategory(final Subject subject)
	{
		categories.add(subject);
	}

	public void addPublisher(final Party publisher)
	{
		publishers.add(publisher);
	}

	@Override
	public void addIdentifier(final String key, final String val)
	{
		identifiers.put(key, val);
	}

	public String getCoverUrl()
	{
		return coverUrl;
	}

	public void setCoverUrl(final String coverUrl)
	{
		this.coverUrl = coverUrl;
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

	public Instant getPublishedAt()
	{
		return publishedAt;
	}

	public void setPublishedAt(final Instant publishedAt)
	{
		this.publishedAt = publishedAt;
	}

	public Set<Party> getAuthors()
	{
		return authors;
	}

	public void setAuthors(final Set<Party> authors)
	{
		this.authors = authors;
	}

	public Set<Subject> getCategories()
	{
		return categories;
	}

	public void setCategories(final Set<Subject> categories)
	{
		this.categories = categories;
	}

	public Set<Party> getPublishers()
	{
		return publishers;
	}

	public void setPublishers(final Set<Party> publishers)
	{
		this.publishers = publishers;
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
}
