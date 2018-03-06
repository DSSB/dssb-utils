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
package dssb.utils.pipeable.binding;

import dssb.utils.common.UNulls;
import dssb.utils.pipeable.BindingRule;
import dssb.utils.pipeable.NullSafeOperator;
import dssb.utils.pipeable.Operator;
import dssb.utils.pipeable.Pipeable;
import lombok.val;

/**
 * Default binding.
 * 
 * This binding does to apply the operator is the following:
 * <ol>
 * <li>If the data is not null, apply the operator.</li>
 * <li>If the data is null, only apply if the operator is a NullSafeOperator</li>
 * </ol>
 * <b>NOTE: </b> when apply, change the operator to be run gracefully (throw Fialable exception).
 * 
 * The result from the apply, will be converted to to a Pipeable before feeding to another operator.
 * If that result is already a Pipeable, return it as such.
 * If the result is not a Pipeable, convert and return it as a Pipeable.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class DefaultBinding implements BindingRule {
    
    /** The instance of the default building. */
    public static final BindingRule instance = new DefaultBinding();
    
    @Override
    public <TYPE, RESULT, THROWABLE extends Throwable> RESULT operate(
            Operator<TYPE, RESULT, THROWABLE> operator,
            Pipeable<TYPE>                    pipe) {
        val rawData = (pipe != null) ? pipe._data() : null;
        if (!(operator instanceof NullSafeOperator)
          && (rawData == null))
            return null;
        
        val rawResult = operator.gracefully().apply(rawData);
        return rawResult;
    }
    
    @Override
    public <TYPE, RESULT, THROWABLE extends Throwable> Pipeable<RESULT> operateToPipe(
            Operator<TYPE, RESULT, THROWABLE> operator,
            Pipeable<TYPE>                    pipe) {
        val rawResult = operate(operator, pipe);
        return UNulls.mapTo(rawResult, Pipeable::of);
    }
    
}
