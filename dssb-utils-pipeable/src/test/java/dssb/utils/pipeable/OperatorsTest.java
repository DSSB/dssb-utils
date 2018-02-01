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

import static dssb.utils.pipeable.Operators.or;
import static dssb.utils.pipeable.Operators.to;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import lombok.val;

@SuppressWarnings("javadoc")
public class OperatorsTest {
    
    @Test
    public void testTo() {
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
