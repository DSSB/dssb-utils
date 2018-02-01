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

/**
 * Operators implementing this interface will be given the data even if it was null.
 * 
 * @param <TYPE>      the type of data that this pipeable is holding.
 * @param <RESULT>    the type of the operation result.
 * @param <THROWABLE> an exception thrown if there is a problem.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@FunctionalInterface
public interface NullSafeOperator<TYPE, RESULT, THROWABLE extends Throwable> extends Operator<TYPE, RESULT, THROWABLE> {
    
}