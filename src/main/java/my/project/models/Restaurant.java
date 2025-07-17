package my.project.models;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id", nullable = false)
	private Integer id;

	@Size(max = 255)
	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull
	@Column(name = "opening_time", nullable = false)
	private LocalTime openingTime;

	@NotNull
	@Column(name = "closing_time", nullable = false)
	private LocalTime closingTime;

	@Lob
	@Column(name = "photo")
	private String photo;

	@Lob
	@Column(name = "description")
	private String description;

	@Size(max = 255)
	@NotNull
	@Column(name = "address", nullable = false)
	private String address;

	@Size(max = 20)
	@NotNull
	@Column(name = "admin_phone", nullable = false, length = 20)
	private String adminPhone;

	@Lob
	@Column(name = "floor_plan")
	private String floorPlan;

	@ColumnDefault("0")
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@OneToMany(mappedBy = "restaurant")
	private Set<Dish> dishes = new LinkedHashSet<>();

	@OneToMany(mappedBy = "restaurant")
	private Set<Order> orders = new LinkedHashSet<>();

	@OneToMany(mappedBy = "restaurant")
	private Set<Tables> tables = new LinkedHashSet<>();

}
