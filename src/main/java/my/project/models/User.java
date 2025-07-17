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

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Integer id;

	@NotNull
	@Lob
	@Column(name = "role", nullable = false)
	private String role;

	@Size(max = 1000)
	@ColumnDefault("https://i.pinimg.com/736x/c4/70/f1/c470f13a66c0597efa80273230847ea1.jpg")
	@Column(name = "photo", length = 1000)
	private String photo;

	@Size(max = 50)
	@NotNull
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Size(max = 50)
	@NotNull
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Size(max = 20)
	@NotNull
	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;

	@Size(max = 100)
	@NotNull
	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Size(max = 255)
	@NotNull
	@Column(name = "password", nullable = false)
	private String password;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private Set<Order> orders = new LinkedHashSet<>();

}
