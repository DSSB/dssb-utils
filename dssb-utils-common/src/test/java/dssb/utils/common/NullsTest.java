package dssb.utils.common;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import lombok.val;
import lombok.experimental.ExtensionMethod;


@SuppressWarnings("javadoc")
@ExtensionMethod({ Nulls.class })
public class NullsTest {
    
    private static Predicate<String> contains(String needle) {
        return heystack-> { 
            return heystack.isNotNull()
                && heystack.contains(needle);
        };
    }
    
    private static Integer IntOf(int i) {
        return Integer.valueOf(i);
    }
    
    @Test
    public void testWhen() {
        val theOriginalString = "The original string";
        val nullString = (String)null;
        
        assertEquals("The original string", theOriginalString.when(contains("original")).or("Another string"));
        assertEquals("Another string",      theOriginalString.when(contains("another" )).or("Another string"));
        assertEquals("Another string",      nullString       .when(contains("original")).or("Another string"));
        assertEquals("Another string",      nullString       .when(contains("another" )).or("Another string"));
    }
    
    @Test
    public void testAs() {
        val i = IntOf(1234);
        assertEquals(IntOf(1234), i.as(Integer.class));
        assertEquals(null,        i.as(Double.class));
        assertEquals(0.0,         i.as(Double.class).or(0.0), 0.0);
    }
    
    @Test
    public void testMapTo() {
        val normalString = "String";
        val nullString   = (String)null;
        val itsLength    = (Function<String, Integer>)String::length;
        assertEquals( 6, normalString.mapTo(itsLength).or(-1));
        assertEquals(-1, nullString  .mapTo(itsLength).or(-1));
    }
    
}
