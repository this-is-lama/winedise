package my.project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tables")
public class Tables {

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "table_id", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Size(max = 15)
	@NotNull
	@Column(name = "name", nullable = false, length = 15)
	private String name;

	@NotNull
	@Column(name = "capacity", nullable = false)
	private Integer capacity;

	@Lob
	@Column(name = "description")
	private String description;

	@ColumnDefault("0")
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@ManyToMany(mappedBy = "restaurantTables", fetch = FetchType.LAZY)
	private Set<Order> orders = new LinkedHashSet<>();

}
