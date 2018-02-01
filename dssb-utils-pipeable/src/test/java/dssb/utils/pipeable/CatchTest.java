//  ========================================================================
//  Copyright (c) 2017 Direct Solution Software Builders (DSSB).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package dssb.utils.pipeable;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

@SuppressWarnings("javadoc")
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
        val result = person.pipe(Person::burn, Catch.thenGet(()->"blah"));
        assertEquals("blah", result);
    }
    
    @Test
    public void testCatchThenReturnFunction() {
        val person = new Person(null);
        val result = person.pipe(Person::burn, Catch.thenApply(e->e.getMessage()));
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
        person.pipe(Person::burn, Catch.thenIgnore());
    }
    
    @Test
    public void testCatchThenPrintStackTrace() {
        val person = new Person(null);
        person.pipe(Person::burn, Catch.thenPrintStackTrace());
    }
    
}
