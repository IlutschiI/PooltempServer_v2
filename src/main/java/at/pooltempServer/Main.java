package at.pooltempServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("at.pooltempServer")
@EntityScan("at.pooltempServer")
@EnableJpaRepositories(basePackages = "at.pooltempServer")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

	}

}
