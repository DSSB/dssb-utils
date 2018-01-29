package dssb.utils.pipeable;

import static dssb.utils.pipeable.Operators.or;
import static dssb.utils.pipeable.Operators.to;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import lombok.val;

public class OperatorsTest {
    
    @Test
    public void testTo() {
        val map = new HashMap<String,String>();
        val str = (Pipeable<String>)()->"Hello";
        assertEquals(5, str.pipe(to(String::length)).intValue());
    }
    
    @Test
    public void testOr() {
        val map = new HashMap<String,String>();
        val str = (Pipeable<String>)()->"Hello";
        assertEquals("Hi", str.pipe(map::get, or("Hi")));
    }
    
}
