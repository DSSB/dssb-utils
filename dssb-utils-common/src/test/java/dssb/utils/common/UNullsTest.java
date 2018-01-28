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
package dssb.utils.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.Test;

import lombok.val;
import lombok.experimental.ExtensionMethod;


@SuppressWarnings("javadoc")
@ExtensionMethod({ UNulls.class })
public class UNullsTest {
    
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
    public void testIsNull() {
        val someString = "String";
        val nullString = (String)null;
        assertFalse(someString.isNull());
        assertTrue(nullString.isNull());
    }
    
    @Test
    public void testIsNotNull() {
        val someString = "String";
        val nullString = (String)null;
        assertTrue(someString.isNotNull());
        assertFalse(nullString.isNotNull());
    }
    
    @Test
    public void testOr() {
        val someString = "String";
        val nullString = (String)null;
        assertEquals("String",         someString.or("Another String"));
        assertEquals("Another String", nullString.or("Another String"));
    }
    
    @Test
    public void testOr_subClass() {
        StringBuffer  someBuffer = new StringBuffer("String");
        StringBuffer  nullBuffer = null;
        CharSequence  someResult = someBuffer.or("Another String");
        CharSequence  nullResult = nullBuffer.or("Another String");
        assertEquals("String",           someResult.toString());
        assertEquals("Another String",   nullResult.toString());
        // Notice that <StringBuffer>.or(<String>) is <CharSequence> which is the closest common ancestor.
    }
    
    @Test
    public void testOrGet() {
        Supplier<String> erorrMessage =  ()->"Another" + " " + "String";
        val someString = "String";
        val nullString = (String)null;
        assertEquals("String",         someString.orGet(erorrMessage));
        assertEquals("Another String", nullString.orGet(erorrMessage));
    }
    
    @Test
    public void testToOptional() {
        val someString = "String";
        val nullString = (String)null;
        assertTrue(someString.toOptional() instanceof Optional);
        assertTrue(someString.toOptional().isPresent());
        assertEquals("String", someString.toOptional().get());
        
        assertTrue(nullString.toOptional() instanceof Optional);
        assertFalse(nullString.toOptional().isPresent());
    }
    
    @Test
    public void testWhenNotNull() {
        val someString = "String";
        val nullString = (String)null;
        assertTrue(someString.whenNotNull() instanceof Optional);
        assertTrue(someString.whenNotNull().isPresent());
        assertEquals("String", someString.whenNotNull().get());
        
        assertTrue(nullString.whenNotNull() instanceof Optional);
        assertFalse(nullString.whenNotNull().isPresent());
    }
    
    private static Consumer<String> saveStringTo(AtomicReference<String> ref) {
        return str -> ref.set(str);
    }
    
    @Test
    public void testWhenNotNull_action() {
        val someString = "String";
        val nullString = (String)null;
        val someResult = new AtomicReference<String>("NULL");
        val nullResult = new AtomicReference<String>("NULL");
        someString.whenNotNull(saveStringTo(someResult));
        nullString.whenNotNull(saveStringTo(nullResult));
        
        assertEquals("String", someResult.get());
        assertEquals("NULL",   nullResult.get());
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
    
    @Test
    public void testMapBy() {
        val normalString = "String";
        val nullString   = (String)null;
        val surroundingWithQuotes = (Function<String, String>)(str->("\'" + str + "\'"));
        assertEquals("\'String\'", normalString.mapBy(surroundingWithQuotes).or("null"));
        assertEquals("null",       nullString  .mapBy(surroundingWithQuotes).or("null"));
    }
    
    @Test
    public void testMapFrom() {
        val normalString = "42";
        val nullString   = (String)null;
        val stringToInt = (Function<String, Integer>)(str->Integer.parseInt(str));
        assertEquals(42, normalString.mapFrom(stringToInt).or(0));
        assertEquals( 0, nullString  .mapFrom(stringToInt).or(0));
    }
    
    @Test
    public void testOrPrimitive() {
        val nullInteger        = (Integer)null;
        val nullLong           = (Long)null;
        val nullDouble         = (Double)null;
        val nullBigInteger     = (BigInteger)null;
        val minusOneBigInteger = BigInteger.valueOf(-1);
        val nullBigDecimal     = (BigDecimal)null;
        val minusOneBigDecimal = BigDecimal.valueOf(-1);
        assertEquals(-1,   nullInteger.or(-1));
        assertEquals(-1L,  nullLong.or(-1L));
        assertEquals(-1L,  nullLong.or(-1));
        assertEquals(-1.0, nullDouble.or(-1.0), 0.0);
        assertEquals(-1.0, nullDouble.or(-1),   0.0);
        assertEquals(minusOneBigInteger, nullBigInteger.or(-1));
        assertEquals(minusOneBigInteger, nullBigInteger.or(-1L));
        assertEquals(minusOneBigDecimal, nullBigDecimal.or(-1));
        assertEquals(minusOneBigDecimal, nullBigDecimal.or(-1L));
        assertEquals(minusOneBigDecimal.doubleValue(), nullBigDecimal.or(-1.0).doubleValue(), 0.0);
    }
    
}
