package dssb.utils.pipeable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import dssb.failable.FailableException;
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
        
        public String burn() throws IOException {
            throw new IOException();
        }
        public String runtimeBurn() throws IOException {
            throw new NullPointerException();
        }
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
    public void testException() {
        val person = new Person(null);
        try {
            person.pipe(Person::burn, String::length);
        } catch (FailableException e) {
            assertTrue(e.getCause() instanceof IOException);
        }
    }
    
    @Test
    public void testException_runtime() {
        val person = new Person(null);
        try {
            person.pipe(Person::runtimeBurn, String::length);
        } catch (FailableException e) {
            assertTrue(e.getCause() instanceof NullPointerException);
        }
    }
    
}
