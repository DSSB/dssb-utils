package dssb.utils.common;

import static org.junit.Assert.*;

import org.junit.Test;

import lombok.val;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("javadoc")
@ExtensionMethod({ Strings.class })
public class StringsTest {
    
    @Test
    public void testIsEmpty() {
        val str = "Str";
        assertFalse(str.isNullOrEmpty());
        
        val nullStr = (String)null;
        assertTrue(nullStr.isNullOrEmpty());
        
        val emptyStr = "";
        assertTrue(emptyStr.isNullOrEmpty());
    }
    
    @Test
    public void testIsBlank() {
        val str = "Str";
        assertFalse(str.isNullOrBlank());
        
        val nullStr = (String)null;
        assertTrue(nullStr.isNullOrBlank());
        
        val emptyStr = "";
        assertTrue(emptyStr.isNullOrBlank());
        
        val whiteSpaceStr = " ";
        assertTrue(whiteSpaceStr.isNullOrBlank());
    }
    
}
