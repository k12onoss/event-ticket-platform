package app.k12onos.tickets;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class TicketsApplicationTests {

	@Test
	void contextLoads() {}

}
