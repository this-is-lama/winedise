package my.project.models;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_dishes")
public class OrderDishes {

	@EmbeddedId
	private OrderDishesId id;

	@MapsId
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@MapsId
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dish_id", nullable = false)
	private Dish dish;

	@NotNull
	@ColumnDefault("1")
	@Column(name = "quantity", nullable = false)
	private Integer quantity;

}
