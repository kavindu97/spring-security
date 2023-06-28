package security.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import security.example.security.model.Role;
import security.example.security.model.User;
import security.example.security.repository.RoleRepository;
import security.example.security.repository.UserRepository;
import security.example.security.service.UserService;

import java.util.HashSet;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class SecurityApplication {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}




	@Bean
	CommandLineRunner run(UserService userService){
		Long countRole=roleRepository.count();
		Long countUser=userRepository.count();

		return args -> {
			if(countUser==0 && countRole==0) {
				userService.saveRole(new Role(null, "ROLE_USER", "this is User"));
				userService.saveRole(new Role(null, "ROLE_ADMIN", "this is Admin"));
				userService.saveRole(new Role(null, "ROLE_MANAGER", "this is Manager"));
				userService.saveRole(new Role(null,"Super_ADMIN","this is super admin"));

				userService.saveUser(new User("4545", "Kavi", "kavindusenarath@gmail.com", "19970629", new HashSet<>()));
				userService.saveUser(new User("4545445", "Kavia", "kavindusenaratha@gmail.com", "19970629a", new HashSet<>()));
				userService.saveUser(new User("4545", "Kavim", "kavindusenarathm@gmail.com", "19970629m", new HashSet<>()));
				userService.saveUser(new User("12345","KaviSup","superadmin@gmail.com","123",new HashSet<>()));


				userService.addToUser("kavindusenarath@gmail.com", "ROLE_USER");
				userService.addToUser("kavindusenaratha@gmail.com", "ROLE_ADMIN");
				userService.addToUser("kavindusenarathm@gmail.com", "ROLE_MANAGER");
				userService.addToUser("superadmin@gmail.com","Super_ADMIN");
			}
		};

	}

}
