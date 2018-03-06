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
 * Binding rule define what to do between each operators and how the final result is created.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public interface BindingRule {
    
    /**
     * Process the given operator to the type for creating the final result.
     * 
     * @param <TYPE>      the input type of the operator.
     * @param <RESULT>    the result type of the operator.
     * @param <THROWABLE> the exception type of the operator.
     * @param operator    the operator.
     * @param pipe        the pipe -- the pipe object can be null - to represent null value.
     * @return            the final result.
     */
    public <TYPE, RESULT, THROWABLE extends Throwable> RESULT operate(
            Operator<TYPE, RESULT, THROWABLE> operator,
            Pipeable<TYPE>                    pipe);
    
    /**
     * Process the given operator to the type for create the result and convert it to a pipeable
     *   (can return null to represent null data).
     * 
     * @param <TYPE>       the data type.
     * @param <RESULT>     the result data type.
     * @param <THROWABLE>  the exception that the operator might throw.
     * @param operator     the operator.
     * @param pipe         the pipe.
     * @return             the result pipe.
     */
    public <TYPE, RESULT, THROWABLE extends Throwable> Pipeable<RESULT> operateToPipe(
            Operator<TYPE, RESULT, THROWABLE> operator,
            Pipeable<TYPE>                    pipe);
    
}
