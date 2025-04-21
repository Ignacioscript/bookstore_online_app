package validation;

import org.ignacioScript.co.util.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorTest {

    @Test
    public void returnCapaitalizeName(){
        assertEquals("Oscar Bob", StringUtils.capitalizeName("OSCAR bob"));
    }

    @Test
    public void returnLowerCase() {
        assertEquals("mail@example.com", StringUtils.toLowerCase("MAIl@Example.com"));
    }
}
