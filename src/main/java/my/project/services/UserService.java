package my.project.services;

import my.project.models.User;
import my.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	private User currentUser;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public String login(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return "user not found";
		}
		if (!user.getPassword().equals(password)) {
			return "wrong password";
		}
		this.currentUser = user;
		return "OK";
	}


	@Transactional
	public boolean register(User user) {
		if (userRepository.findByEmail(user.getEmail()) != null) {
			return false;
		}
		user.setRole("customer");
		user.setPhoto("https://i.pinimg.com/736x/c4/70/f1/c470f13a66c0597efa80273230847ea1.jpg");
		this.currentUser = userRepository.save(user);
		return true;
	}

	@Transactional
	public void makeAdmin(int id) {
		User user = userRepository.findById(id);
		user.setRole("admin");
		userRepository.save(user);
	}

	@Transactional
	public void exit() {
		this.currentUser = null;
	}

	@Transactional
	public void edit(User user) {
		String name = user.getFirstName() != null ? user.getFirstName() : currentUser.getFirstName();
		currentUser.setFirstName(name);
		String lastName = user.getLastName() != null ? user.getLastName() : currentUser.getLastName();
		currentUser.setLastName(lastName);
		String photo = user.getPhoto() != null ? user.getPhoto() : currentUser.getPhoto();
		currentUser.setPhoto(photo);
		String email = user.getEmail() != null ? user.getEmail() : currentUser.getEmail();
		currentUser.setEmail(email);
		String password = user.getPassword() != null ? user.getPassword() : currentUser.getPassword();
		currentUser.setPassword(password);
		String phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber() : currentUser.getPhoneNumber();
		currentUser.setPhoneNumber(phoneNumber);
		String role = user.getRole() != null ? user.getRole() : currentUser.getRole();
		currentUser.setRole(role);
		this.currentUser = userRepository.save(currentUser);
	}

	@Transactional
	public void delete() {
		userRepository.delete(currentUser);
		this.currentUser = null;
	}

	public User getCurrentUser() {
		if (this.currentUser == null) {
			return null;
		}
		int id = currentUser.getId();
		return userRepository.findById(id);
	}
}
