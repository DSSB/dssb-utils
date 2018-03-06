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

import lombok.val;

/**
 * Helper for processing operator for a pipe.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class PipeHelper {
    
    /**
     * Process the given operator to the type for creating the final result.
     * 
     * @param <TYPE>      the input type of the operator.
     * @param <RESULT>    the result type of the operator.
     * @param <THROWABLE> the exception type of the operator.
     * @param operator    the operator.
     * @param pipe        the pipe.
     * @return            the final result.
     */
    public static <TYPE, RESULT, THROWABLE extends Throwable> RESULT operateToResult(
            Operator<TYPE, RESULT, THROWABLE> operator,
            Pipeable<TYPE>                    pipe) {
        val rawData = (pipe != null) ? pipe._data() : null;
        if (!(operator instanceof NullSafeOperator)
          && (rawData == null))
            return null;
        
        val rawResult = operator.gracefully().apply(rawData);
        return rawResult;
    }
    
    /**
     * This method will be executed if this operation is not the last in the pipe;
     *   thus, creating another pipe value to be passed on to the next operator.
     * 
     * This method has a default implementation that correctly handle null value.
     * So if this method is to be overwritten, the new implementation should handle that too.
     * 
     * @param <OTYPE>      the input type of the operator.
     * @param <ORESULT>    the result type of the operator.
     * @param <OTHROWABLE> the exception type of the operator.
     * @param rawResult    the result to be process to pipe.
     * @return             the pipe result of the operation.
     */
    @SuppressWarnings("unchecked")
    public static <OTYPE, ORESULT, OTHROWABLE extends Throwable> Pipeable<ORESULT> resultToPipe(
            ORESULT rawResult) {
        return (rawResult instanceof Pipeable)
                ? (Pipeable<ORESULT>)rawResult
                : ()->rawResult;
    }
    
}
