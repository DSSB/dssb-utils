//  ========================================================================
//  Copyright (c) 2017 Nawapunth Manusitthipol.
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

import lombok.experimental.UtilityClass;

/**
 * This utility class contains useful methods involving Strings.
 * 
 * @author NawaMan
 */
@UtilityClass
public class Strings {
    
    /**
     * Checks if theGivenString is empty (null or 0-length).
     * 
     * @param  theGivenString  the given string.
     * @return {@code true} if theGivenString is empty.
     */
    public boolean isNullOrEmpty(String theGivenString) {
        return (theGivenString == null) || theGivenString.isEmpty();
    }
    
    /**
     * Checks if theGivenString is blank (its trim is empty).
     * 
     * @param  theGivenString  the given string.
     * @return {@code true} if theGivenString is blank.
     */
    public boolean isNullOrBlank(String theGivenString) {
        return (theGivenString == null) || theGivenString.trim().isEmpty();
    }
    
}
