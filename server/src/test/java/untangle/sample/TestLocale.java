package untangle.sample;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

public class TestLocale {
	
	@Test
	void testLocaleString() {
		assertThat( Locale.forLanguageTag("nl-BE")).isEqualTo(new Locale("nl", "BE"));
		
	}
}
