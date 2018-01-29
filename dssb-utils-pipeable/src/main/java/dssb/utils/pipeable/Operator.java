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


import dssb.failable.Failable;
import lombok.val;

@FunctionalInterface
public interface Operator<TYPE, RESULT, THROWABLE extends Throwable> extends Failable.Function<TYPE, RESULT, THROWABLE> {
    
    public RESULT apply(TYPE data) throws THROWABLE;
    
    public default RESULT operateToResult(Pipeable<TYPE> pipe) {
        val rawData = pipe._data();
        if (!(this instanceof NullSafeOperator)
          && (rawData == null))
            return null;
        
        val rawResult = this.gracefully().apply(rawData);
        return rawResult;
    }
    
    public default Pipeable<RESULT> operateToPipe(Pipeable<TYPE> pipe) {
        val rawData = pipe._data();
        if (!(this instanceof NullSafeOperator)
          && (rawData == null))
            return null;
        
        val rawResult = this.gracefully().apply(rawData);
        return (rawResult instanceof Pipeable)
                ? (Pipeable<RESULT>)rawResult
                : ()->rawResult;
    }
    
}