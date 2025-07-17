package my.project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderTablesId implements java.io.Serializable {
	@Serial
	private static final long serialVersionUID = 2790441095776702049L;
	@NotNull
	@Column(name = "order_id", nullable = false)
	private Integer orderId;

	@NotNull
	@Column(name = "table_id", nullable = false)
	private Integer tableId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		OrderTablesId entity = (OrderTablesId) o;
		return Objects.equals(this.orderId, entity.orderId) &&
				Objects.equals(this.tableId, entity.tableId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, tableId);
	}

}