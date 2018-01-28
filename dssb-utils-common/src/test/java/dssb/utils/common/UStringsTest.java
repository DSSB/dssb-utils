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

import static org.junit.Assert.*;

import org.junit.Test;

import lombok.val;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("javadoc")
@ExtensionMethod({ UStrings.class })
public class UStringsTest {
    
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
    
    @Test
    public void testTrimToNull() {
        val str = "\t Str ";
        assertEquals("Str", str.trimToNull());
        
        val nullStr = (String)null;
        assertNull(nullStr.trimToNull());
        
        val emptyStr = "";
        assertNull(emptyStr.trimToNull());
        
        val whiteSpaceStr = " ";
        assertNull(whiteSpaceStr.trimToNull());
    }
    
    @Test
    public void testTrimToEmpty() {
        val str = "\t Str ";
        assertEquals("Str", str.trimToEmpty());
        
        val nullStr = (String)null;
        assertTrue(nullStr.trimToEmpty().isEmpty());
        
        val emptyStr = "";
        assertTrue(emptyStr.trimToEmpty().isEmpty());
        
        val whiteSpaceStr = " ";
        assertTrue(whiteSpaceStr.trimToEmpty().isEmpty());
    }
    
}
