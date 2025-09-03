package app.k12onos.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@Theme
public class TicketsApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(TicketsApplication.class, args);
	}

}
