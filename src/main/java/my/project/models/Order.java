package my.project.models;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id", nullable = false)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@Column(name = "people_count", nullable = false)
	private Integer peopleCount;

	@NotNull
	@Column(name = "reservation_start", nullable = false)
	private LocalTime reservationStart;

	@NotNull
	@Column(name = "reservation_end", nullable = false)
	private LocalTime reservationEnd;

	@NotNull
	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Lob
	@Column(name = "special_requests")
	private String specialRequests;

	@NotNull
	@Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@OneToMany(mappedBy = "order", orphanRemoval = true)
	private Set<OrderDishes> orderDishes = new LinkedHashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "order_tables",
			joinColumns = @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name = "table_id"))
	private Set<Tables> restaurantTables = new LinkedHashSet<>();

	public Order(LocalDate date, LocalTime start, LocalTime end, Integer count) {
		this.date = date;
		this.reservationStart = start;
		this.reservationEnd = end;
		this.peopleCount = count;
	}
}
