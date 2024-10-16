package no.hvl.dat250.polls;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PollsApplicationTests {

	@Test
	void contextLoads() {
	    System.out.println("Done");
	}

}
