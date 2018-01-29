package dssb.utils.pipeable;

import static dssb.utils.pipeable.Operators.or;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import dssb.utils.common.UNulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

public class PipeableTest {
    
    @Test
    public void testBasicPiping() {
        val str = (Pipeable<String>)()->"Str";
        assertEquals(3, str.pipe(String::length).intValue());
    }
    
    @Data
    @Accessors(fluent=true, chain=true)
    @AllArgsConstructor
    static class Person implements IPipe<Person> {
        private String name;
    }
    
    @Test
    public void testChainPiping() {
        val person = new Person("Alice");
        assertEquals(5,
                person.pipe(
                    Person::name,
                    String::length)
                .intValue());
        
        assertEquals("false",
                person.pipe(
                    Person::name,
                    String::isEmpty,
                    Object::toString));
    }
    
    @Test
    public void testNull() {
        val person = new Person(null);
        assertNull(person.pipe(Person::name, String::length));
    }
    
    @Test
    public void testCatch() {
//        Assert.fail();
    }
    
}
