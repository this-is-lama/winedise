package my.project.models;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "dishes")
public class Dish {

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dish_id", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Size(max = 100)
	@NotNull
	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Size(max = 1000)
	@ColumnDefault("https://i.pinimg.com/736x/ff/88/77/ff88775874a53fd1420a4486db58b4d9.jpg")
	@Column(name = "photo", length = 1000)
	private String photo;

	@NotNull
	@Column(name = "price", nullable = false, precision = 10, scale = 2)
	private Integer price;

	@Size(max = 100)
	@NotNull
	@Column(name = "category", nullable = false, length = 100)
	private String category;

	@Column(name = "weight_grams")
	private Integer weightGrams;

	@Lob
	@Column(name = "description")
	private String description;

	@Lob
	@Column(name = "ingredients")
	private String ingredients;

	@Column(name = "calories")
	private Integer calories;

	@Column(name = "proteins", precision = 5, scale = 2)
	private BigDecimal proteins;

	@Column(name = "fats", precision = 5, scale = 2)
	private BigDecimal fats;

	@Column(name = "carbohydrates", precision = 5, scale = 2)
	private BigDecimal carbohydrates;

	@Column(name = "preparation_time_minutes")
	private Integer preparationTimeMinutes;

	@ColumnDefault("0")
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany(mappedBy = "dish")
	private Set<OrderDishes> orderDishes = new LinkedHashSet<>();

}
