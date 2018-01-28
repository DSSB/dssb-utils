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

import lombok.val;

/**
 * This utility class contains useful methods involving Strings.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class UStrings {
    
    /**
     * Checks if theGivenString is empty (null or 0-length).
     * 
     * @param  theGivenString  the given string.
     * @return {@code true} if theGivenString is empty.
     */
    public static boolean isNullOrEmpty(String theGivenString) {
        return (theGivenString == null) || theGivenString.isEmpty();
    }
    
    /**
     * Checks if theGivenString is blank (its trim is empty).
     * 
     * @param  theGivenString  the given string.
     * @return {@code true} if theGivenString is blank.
     */
    public static boolean isNullOrBlank(String theGivenString) {
        return (theGivenString == null) || theGivenString.trim().isEmpty();
    }
    
    /**
     * Trim the given string and if the result is an empty string, return null.
     * 
     * @param theGivenString  the given string.
     * @return  {@code null} if the given string is null or its trimmed value is empty.
     */
    public static String trimToNull(String theGivenString) {
        if (theGivenString == null)
            return null;
        
        val trimmedString = theGivenString.trim();
        if (trimmedString.isEmpty())
            return null;
        
        return trimmedString;
    }
    
    /**
     * Trim and return the given string. If the string is null, return empty string.
     * 
     * @param theGivenString  the given string.
     * @return  {@code ""} if the given string is null or its trimmed value.
     */
    public static String trimToEmpty(String theGivenString) {
        if (theGivenString == null)
            return "";
        
        val trimmedString = theGivenString.trim();
        return trimmedString;
    }
    
}
