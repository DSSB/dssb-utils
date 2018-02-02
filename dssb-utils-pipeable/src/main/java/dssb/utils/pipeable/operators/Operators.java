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
package dssb.utils.pipeable.operators;

import java.util.function.Function;
import java.util.function.Supplier;

import dssb.utils.common.UNulls;
import dssb.utils.pipeable.NullSafeOperator;
import dssb.utils.pipeable.Operator;

/**
 * This class contains example operators.
 * 
 * Do not reply on these operators, create yourown as you need.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class Operators {
    
    /**
     * This operator simply call the given function.
     * 
     * This is not a very useful operator as the mapping function itself already done this.
     * Use this operator only to improve readability to your code -- when it actual does, of course.
     * 
     * @param function  the function.
     * @return  the operator for the given function.
     */
    public static <T, R, THROWABLE extends Throwable>
                Operator<T, R, THROWABLE> to(Function<T, R> function) {
        return t->function.apply(t);
    }
    
    /**
     * This operator simply convert the value to string.
     * 
     * @return  the operator to convert oject to string.
     */
    public static <T> Operator<T, String, RuntimeException> toStr() {
        return Object::toString;
    }
    
    /**
     * This null-safe operator return the default value if the pipe value is null.
     * 
     * @param defaultValue  the default value to be returned.
     * @return  the OR operator.
     */
    public static <T, THROWABLE extends Throwable> NullSafeOperator<T, T, THROWABLE> or(T defaultValue) {
        return t->UNulls.or(t, defaultValue);
    }
    
    /**
     * This null-safe operator return the default value if the pipe value is null.
     * 
     * @param defaultSupplier  the default value to be returned.
     * @return  the OR operator.
     */
    public static <T, THROWABLE extends Throwable> NullSafeOperator<T, T, THROWABLE> orGet(Supplier<T> defaultSupplier) {
        return t->UNulls.orGet(t, defaultSupplier);
    }
    
    /**
     * This null-safe operator return the default value if the pipe value is null.
     * 
     * @param defaultValue  the default value to be returned.
     * @return  the OR operator.
     */
    public static <T, THROWABLE extends Throwable> NullSafeOperator<T, T, THROWABLE> otherwise(T defaultValue) {
        return t->UNulls.or(t, defaultValue);
    }
    
    /**
     * This null-safe operator return the default value if the pipe value is null.
     * 
     * @param defaultSupplier  the default value to be returned.
     * @return  the OR operator.
     */
    public static <T, THROWABLE extends Throwable> NullSafeOperator<T, T, THROWABLE> otherwiseGet(Supplier<T> defaultSupplier) {
        return t->UNulls.orGet(t, defaultSupplier);
    }
    
}
