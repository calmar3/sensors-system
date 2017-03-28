package configuration;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = rest.RestController.class)
@ComponentScan(basePackageClasses = SensorsConfiguration.class)
public class StreetLampSensorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreetLampSensorsApplication.class, args);
	}
}
