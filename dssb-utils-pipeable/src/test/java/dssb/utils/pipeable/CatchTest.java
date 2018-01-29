package dssb.utils.pipeable;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import dssb.failable.FailableException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

public class CatchTest {
    
    @Data
    @Accessors(fluent=true, chain=true)
    @AllArgsConstructor
    static class Person implements IPipe<Person> {
        private String name;
        
        public String burn() throws IOException {
            throw new IOException("burned");
        }
    }
    
    @Test
    public void testCatchThen() throws Throwable {
        val person = new Person(null);
        @SuppressWarnings("unchecked")
        val result = person.pipe(Person::burn, Catch.then(e->e.toString()));
        assertEquals("dssb.failable.FailableException: java.io.IOException: burned", result);
    }
    
    @Test
    public void testCatchThenReturn() {
        val person = new Person(null);
        val result = person.pipe(Person::burn, Catch.thenReturn("blah"));
        assertEquals("blah", result);
    }
    
    @Test
    public void testCatchThenReturnSupplier() {
        val person = new Person(null);
        val result = person.pipe(Person::burn, Catch.thenReturn(()->"blah"));
        assertEquals("blah", result);
    }
    
    @Test
    public void testCatchThenReturnFunction() {
        val person = new Person(null);
        val result = person.pipe(Person::burn, Catch.thenReturn(e->e.getMessage()));
        assertEquals("java.io.IOException: burned", result);
    }
    
    @Test(expected=IOException.class)
    public void testCatchThenThrow() throws Throwable {
        val person = new Person(null);
        person.pipe(Person::burn, Catch.thenThrow());
    }
    
    @Test
    public void testCatchThenIgnore() {
        val person = new Person(null);
        person.pipe(Person::burn, Catch.thenIgnore());  // TODO - Why it has yellow line.
    }
    
    @Test
    public void testCatchThenPrintStackTrace() {
        val person = new Person(null);
        person.pipe(Person::burn, Catch.thenPrintStackTrace());  // TODO - Why it has yellow line.
    }
    
}
