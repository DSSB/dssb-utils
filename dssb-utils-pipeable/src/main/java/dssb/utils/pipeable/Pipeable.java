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

import java.util.function.Supplier;

import dssb.failable.FailableException;
import dssb.utils.pipeable.binding.DefaultBinding;
import dssb.utils.pipeable.supportive.CatchNoCheckException;
import lombok.val;

/**
 * Classes implementing this interface can perform piping.
 * 
 * Piping are functional binding construct that allows an object to be passed on through a sequence of operations.
 * Each operations transform the object to a new one and pass along to aother operation in the sequence.
 * This is very similar to Unix piping.
 * 
 * The piping also detect error occurred as the object is passed along.
 * If at the end of the pipe, no exception catching was defined,
 *   the exception will be thrown wrapped by a FailableException.
 * If a catcher was defined, the exception will be passed to the catcher to handled.
 * 
 * By default, Operator will ignore the data value null.
 * Except for NullSafeOperator which will take the null.
 *
 * @param <TYPE> the type of data that this pipeable is holding.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@FunctionalInterface
public interface Pipeable<TYPE> {
    
    /**
     * Returns the wrapped data.
     * 
     * @return the wrapped data.
     */
    public TYPE _data();
    
    
    /**
     * Returns the binding rule to use.
     * 
     * @return the binding rule.
     */
    public default BindingRule _bindingRule() {
        return DefaultBinding.instance;
    }
    
    // == Constructor ==
    
    /**
     * Create the pipeable of the data
     * 
     * @param <TYPE>  the data type.
     * @param data    the data.
     * @return the pipeable of the data.
     */
    public static <TYPE> Pipeable<TYPE> of(TYPE data) {
        return ()->data;
    }
    
    /**
     * Create the pipeable of the data
     * 
     * @param <TYPE>    the data type.
     * @param supplier  the supplier of the data.
     * @return the pipeable of the data.
     */
    public static <TYPE> Pipeable<TYPE> from(Supplier<TYPE> supplier) {
        return ()->supplier.get();
    }
    
    //== Piping methods ==
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE, RESULT, THROWABLE> operator1) {
        val binding = _bindingRule();
        val result = binding.operate(operator1, this);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE, RESULT, THROWABLE>  operator1,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val result = binding.operate(operator1, this);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   RESULT,  THROWABLE>  operator1,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val result = binding.operate(operator1, this);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  RESULT,  THROWABLE>  operator2) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val result = binding.operate(operator2, pipe1);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  RESULT,  THROWABLE>  operator2,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val result = binding.operate(operator2, pipe1);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  RESULT,  THROWABLE>  operator2,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val result = binding.operate(operator2, pipe1);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  RESULT,  THROWABLE>  operator3) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val pipe2  = binding.operateToPipe(operator2, pipe1);
        val result = binding.operate(operator3, pipe2);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  RESULT,  THROWABLE>  operator3,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val result = binding.operate(operator3, pipe2);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  RESULT,  THROWABLE>  operator3,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val result = binding.operate(operator3, pipe2);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  RESULT,  THROWABLE>  operator4) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val pipe2  = binding.operateToPipe(operator2, pipe1);
        val pipe3  = binding.operateToPipe(operator3, pipe2);
        val result = binding.operate(operator4, pipe3);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  RESULT,  THROWABLE>  operator4,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val result = binding.operate(operator4, pipe3);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  RESULT,  THROWABLE>  operator4,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val result = binding.operate(operator4, pipe3);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  RESULT,  THROWABLE>  operator5) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val pipe2  = binding.operateToPipe(operator2, pipe1);
        val pipe3  = binding.operateToPipe(operator3, pipe2);
        val pipe4  = binding.operateToPipe(operator4, pipe3);
        val result = binding.operate(operator5, pipe4);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  RESULT,  THROWABLE>  operator5,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val result = binding.operate(operator5, pipe4);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  RESULT,  THROWABLE>  operator5,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val result = binding.operate(operator5, pipe4);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  RESULT,  THROWABLE>  operator6) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val pipe2  = binding.operateToPipe(operator2, pipe1);
        val pipe3  = binding.operateToPipe(operator3, pipe2);
        val pipe4  = binding.operateToPipe(operator4, pipe3);
        val pipe5  = binding.operateToPipe(operator5, pipe4);
        val result = binding.operate(operator6, pipe5);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  RESULT,  THROWABLE>  operator6,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val result = binding.operate(operator6, pipe5);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  RESULT,  THROWABLE>  operator6,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val result = binding.operate(operator6, pipe5);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  RESULT,  THROWABLE>  operator7) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val pipe2  = binding.operateToPipe(operator2, pipe1);
        val pipe3  = binding.operateToPipe(operator3, pipe2);
        val pipe4  = binding.operateToPipe(operator4, pipe3);
        val pipe5  = binding.operateToPipe(operator5, pipe4);
        val pipe6  = binding.operateToPipe(operator6, pipe5);
        val result = binding.operate(operator7, pipe6);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  RESULT,  THROWABLE>  operator7,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val result = binding.operate(operator7, pipe6);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  RESULT,  THROWABLE>  operator7,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val result = binding.operate(operator7, pipe6);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  RESULT,  THROWABLE>  operator8) {
        val binding = _bindingRule();
        val pipe1 = binding.operateToPipe(operator1, this);
        val pipe2 = binding.operateToPipe(operator2, pipe1);
        val pipe3 = binding.operateToPipe(operator3, pipe2);
        val pipe4 = binding.operateToPipe(operator4, pipe3);
        val pipe5 = binding.operateToPipe(operator5, pipe4);
        val pipe6 = binding.operateToPipe(operator6, pipe5);
        val pipe7 = binding.operateToPipe(operator7, pipe6);
        val result = binding.operate(operator8, pipe7);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  RESULT,  THROWABLE>  operator8,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val result = binding.operate(operator8, pipe7);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  RESULT,  THROWABLE>  operator8,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val result = binding.operate(operator8, pipe7);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  RESULT,  THROWABLE>  operator9) {
        val binding = _bindingRule();
        val pipe1 = binding.operateToPipe(operator1, this);
        val pipe2 = binding.operateToPipe(operator2, pipe1);
        val pipe3 = binding.operateToPipe(operator3, pipe2);
        val pipe4 = binding.operateToPipe(operator4, pipe3);
        val pipe5 = binding.operateToPipe(operator5, pipe4);
        val pipe6 = binding.operateToPipe(operator6, pipe5);
        val pipe7 = binding.operateToPipe(operator7, pipe6);
        val pipe8 = binding.operateToPipe(operator8, pipe7);
        val result = binding.operate(operator9, pipe8);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  RESULT,  THROWABLE>  operator9,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val pipe8 = binding.operateToPipe(operator8, pipe7);
            val result = binding.operate(operator9, pipe8);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  RESULT,  THROWABLE>  operator9,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val pipe8 = binding.operateToPipe(operator8, pipe7);
            val result = binding.operate(operator9, pipe8);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  RESULT, THROWABLE>   operator10) {
        val binding = _bindingRule();
        val pipe1 = binding.operateToPipe(operator1, this);
        val pipe2 = binding.operateToPipe(operator2, pipe1);
        val pipe3 = binding.operateToPipe(operator3, pipe2);
        val pipe4 = binding.operateToPipe(operator4, pipe3);
        val pipe5 = binding.operateToPipe(operator5, pipe4);
        val pipe6 = binding.operateToPipe(operator6, pipe5);
        val pipe7 = binding.operateToPipe(operator7, pipe6);
        val pipe8 = binding.operateToPipe(operator8, pipe7);
        val pipe9 = binding.operateToPipe(operator9, pipe8);
        val result = binding.operate(operator10, pipe9);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  RESULT, THROWABLE>   operator10,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val pipe8 = binding.operateToPipe(operator8, pipe7);
            val pipe9 = binding.operateToPipe(operator9, pipe8);
            val result = binding.operate(operator10, pipe9);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  RESULT, THROWABLE>   operator10,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val pipe8 = binding.operateToPipe(operator8, pipe7);
            val pipe9 = binding.operateToPipe(operator9, pipe8);
            val result = binding.operate(operator10, pipe9);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, RESULT, THROWABLE>   operator11) {
        val binding = _bindingRule();
        val pipe1 = binding.operateToPipe(operator1, this);
        val pipe2 = binding.operateToPipe(operator2, pipe1);
        val pipe3 = binding.operateToPipe(operator3, pipe2);
        val pipe4 = binding.operateToPipe(operator4, pipe3);
        val pipe5 = binding.operateToPipe(operator5, pipe4);
        val pipe6 = binding.operateToPipe(operator6, pipe5);
        val pipe7 = binding.operateToPipe(operator7, pipe6);
        val pipe8 = binding.operateToPipe(operator8, pipe7);
        val pipe9 = binding.operateToPipe(operator9, pipe8);
        val pipe10 = binding.operateToPipe(operator10, pipe9);
        val result = binding.operate(operator11, pipe10);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, RESULT, THROWABLE>   operator11,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val pipe8 = binding.operateToPipe(operator8, pipe7);
            val pipe9 = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val result = binding.operate(operator11, pipe10);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, RESULT, THROWABLE>   operator11,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val pipe8 = binding.operateToPipe(operator8, pipe7);
            val pipe9 = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val result = binding.operate(operator11, pipe10);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, RESULT, THROWABLE>   operator12) {
        val binding = _bindingRule();
        val pipe1 = binding.operateToPipe(operator1, this);
        val pipe2 = binding.operateToPipe(operator2, pipe1);
        val pipe3 = binding.operateToPipe(operator3, pipe2);
        val pipe4 = binding.operateToPipe(operator4, pipe3);
        val pipe5 = binding.operateToPipe(operator5, pipe4);
        val pipe6 = binding.operateToPipe(operator6, pipe5);
        val pipe7 = binding.operateToPipe(operator7, pipe6);
        val pipe8 = binding.operateToPipe(operator8, pipe7);
        val pipe9 = binding.operateToPipe(operator9, pipe8);
        val pipe10 = binding.operateToPipe(operator10, pipe9);
        val pipe11 = binding.operateToPipe(operator11, pipe10);
        val result = binding.operate(operator12, pipe11);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, RESULT, THROWABLE>   operator12,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val pipe8 = binding.operateToPipe(operator8, pipe7);
            val pipe9 = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val result = binding.operate(operator12, pipe11);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, RESULT, THROWABLE>   operator12,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1 = binding.operateToPipe(operator1, this);
            val pipe2 = binding.operateToPipe(operator2, pipe1);
            val pipe3 = binding.operateToPipe(operator3, pipe2);
            val pipe4 = binding.operateToPipe(operator4, pipe3);
            val pipe5 = binding.operateToPipe(operator5, pipe4);
            val pipe6 = binding.operateToPipe(operator6, pipe5);
            val pipe7 = binding.operateToPipe(operator7, pipe6);
            val pipe8 = binding.operateToPipe(operator8, pipe7);
            val pipe9 = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val result = binding.operate(operator12, pipe11);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, RESULT, THROWABLE>   operator13) {
        val binding = _bindingRule();
        val pipe1 = binding.operateToPipe(operator1, this);
        val pipe2 = binding.operateToPipe(operator2, pipe1);
        val pipe3 = binding.operateToPipe(operator3, pipe2);
        val pipe4 = binding.operateToPipe(operator4, pipe3);
        val pipe5 = binding.operateToPipe(operator5, pipe4);
        val pipe6 = binding.operateToPipe(operator6, pipe5);
        val pipe7 = binding.operateToPipe(operator7, pipe6);
        val pipe8 = binding.operateToPipe(operator8, pipe7);
        val pipe9 = binding.operateToPipe(operator9, pipe8);
        val pipe10 = binding.operateToPipe(operator10, pipe9);
        val pipe11 = binding.operateToPipe(operator11, pipe10);
        val pipe12 = binding.operateToPipe(operator12, pipe11);
        val result = binding.operate(operator13, pipe12);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, RESULT, THROWABLE>   operator13,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val pipe7  = binding.operateToPipe(operator7, pipe6);
            val pipe8  = binding.operateToPipe(operator8, pipe7);
            val pipe9  = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val pipe12 = binding.operateToPipe(operator12, pipe11);
            val result = binding.operate(operator13, pipe12);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, RESULT, THROWABLE>   operator13,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val pipe7  = binding.operateToPipe(operator7, pipe6);
            val pipe8  = binding.operateToPipe(operator8, pipe7);
            val pipe9  = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val pipe12 = binding.operateToPipe(operator12, pipe11);
            val result = binding.operate(operator13, pipe12);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, RESULT, THROWABLE>   operator14) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val pipe2  = binding.operateToPipe(operator2, pipe1);
        val pipe3  = binding.operateToPipe(operator3, pipe2);
        val pipe4  = binding.operateToPipe(operator4, pipe3);
        val pipe5  = binding.operateToPipe(operator5, pipe4);
        val pipe6  = binding.operateToPipe(operator6, pipe5);
        val pipe7  = binding.operateToPipe(operator7, pipe6);
        val pipe8  = binding.operateToPipe(operator8, pipe7);
        val pipe9  = binding.operateToPipe(operator9, pipe8);
        val pipe10 = binding.operateToPipe(operator10, pipe9);
        val pipe11 = binding.operateToPipe(operator11, pipe10);
        val pipe12 = binding.operateToPipe(operator12, pipe11);
        val pipe13 = binding.operateToPipe(operator13, pipe12);
        val result = binding.operate(operator14, pipe13);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, RESULT, THROWABLE>   operator14,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val pipe7  = binding.operateToPipe(operator7, pipe6);
            val pipe8  = binding.operateToPipe(operator8, pipe7);
            val pipe9  = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val pipe12 = binding.operateToPipe(operator12, pipe11);
            val pipe13 = binding.operateToPipe(operator13, pipe12);
            val result = binding.operate(operator14, pipe13);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, RESULT, THROWABLE>   operator14,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val pipe7  = binding.operateToPipe(operator7, pipe6);
            val pipe8  = binding.operateToPipe(operator8, pipe7);
            val pipe9  = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val pipe12 = binding.operateToPipe(operator12, pipe11);
            val pipe13 = binding.operateToPipe(operator13, pipe12);
            val result = binding.operate(operator14, pipe13);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    TYPE14, THROWABLE14 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, TYPE14, THROWABLE14> operator14,
                        Operator<TYPE14, RESULT, THROWABLE>   operator15) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val pipe2  = binding.operateToPipe(operator2, pipe1);
        val pipe3  = binding.operateToPipe(operator3, pipe2);
        val pipe4  = binding.operateToPipe(operator4, pipe3);
        val pipe5  = binding.operateToPipe(operator5, pipe4);
        val pipe6  = binding.operateToPipe(operator6, pipe5);
        val pipe7  = binding.operateToPipe(operator7, pipe6);
        val pipe8  = binding.operateToPipe(operator8, pipe7);
        val pipe9  = binding.operateToPipe(operator9, pipe8);
        val pipe10 = binding.operateToPipe(operator10, pipe9);
        val pipe11 = binding.operateToPipe(operator11, pipe10);
        val pipe12 = binding.operateToPipe(operator12, pipe11);
        val pipe13 = binding.operateToPipe(operator13, pipe12);
        val pipe14 = binding.operateToPipe(operator14, pipe13);
        val result = binding.operate(operator15, pipe14);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    TYPE14, THROWABLE14 extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, TYPE14, THROWABLE14> operator14,
                        Operator<TYPE14, RESULT, THROWABLE>   operator15,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val pipe7  = binding.operateToPipe(operator7, pipe6);
            val pipe8  = binding.operateToPipe(operator8, pipe7);
            val pipe9  = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val pipe12 = binding.operateToPipe(operator12, pipe11);
            val pipe13 = binding.operateToPipe(operator13, pipe12);
            val pipe14 = binding.operateToPipe(operator14, pipe13);
            val result = binding.operate(operator15, pipe14);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    TYPE14, THROWABLE14 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, TYPE14, THROWABLE14> operator14,
                        Operator<TYPE14, RESULT, THROWABLE>   operator15,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val pipe7  = binding.operateToPipe(operator7, pipe6);
            val pipe8  = binding.operateToPipe(operator8, pipe7);
            val pipe9  = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val pipe12 = binding.operateToPipe(operator12, pipe11);
            val pipe13 = binding.operateToPipe(operator13, pipe12);
            val pipe14 = binding.operateToPipe(operator14, pipe13);
            val result = binding.operate(operator15, pipe14);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    TYPE14, THROWABLE14 extends Throwable,
                    TYPE15, THROWABLE15 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, TYPE14, THROWABLE14> operator14,
                        Operator<TYPE14, TYPE15, THROWABLE15> operator15,
                        Operator<TYPE15, RESULT, THROWABLE>   operator16) {
        val binding = _bindingRule();
        val pipe1  = binding.operateToPipe(operator1, this);
        val pipe2  = binding.operateToPipe(operator2, pipe1);
        val pipe3  = binding.operateToPipe(operator3, pipe2);
        val pipe4  = binding.operateToPipe(operator4, pipe3);
        val pipe5  = binding.operateToPipe(operator5, pipe4);
        val pipe6  = binding.operateToPipe(operator6, pipe5);
        val pipe7  = binding.operateToPipe(operator7, pipe6);
        val pipe8  = binding.operateToPipe(operator8, pipe7);
        val pipe9  = binding.operateToPipe(operator9, pipe8);
        val pipe10 = binding.operateToPipe(operator10, pipe9);
        val pipe11 = binding.operateToPipe(operator11, pipe10);
        val pipe12 = binding.operateToPipe(operator12, pipe11);
        val pipe13 = binding.operateToPipe(operator13, pipe12);
        val pipe14 = binding.operateToPipe(operator14, pipe13);
        val pipe15 = binding.operateToPipe(operator15, pipe14);
        val result = binding.operate(operator16, pipe15);
        return result;
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    TYPE14, THROWABLE14 extends Throwable,
                    TYPE15, THROWABLE15 extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, TYPE14, THROWABLE14> operator14,
                        Operator<TYPE14, TYPE15, THROWABLE15> operator15,
                        Operator<TYPE15, RESULT, THROWABLE>   operator16,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val pipe7  = binding.operateToPipe(operator7, pipe6);
            val pipe8  = binding.operateToPipe(operator8, pipe7);
            val pipe9  = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val pipe12 = binding.operateToPipe(operator12, pipe11);
            val pipe13 = binding.operateToPipe(operator13, pipe12);
            val pipe14 = binding.operateToPipe(operator14, pipe13);
            val pipe15 = binding.operateToPipe(operator15, pipe14);
            val result = binding.operate(operator16, pipe15);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    @SuppressWarnings("javadoc")
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    TYPE9,  THROWABLE9  extends Throwable,
                    TYPE10, THROWABLE10 extends Throwable,
                    TYPE11, THROWABLE11 extends Throwable,
                    TYPE12, THROWABLE12 extends Throwable,
                    TYPE13, THROWABLE13 extends Throwable,
                    TYPE14, THROWABLE14 extends Throwable,
                    TYPE15, THROWABLE15 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  TYPE4,  THROWABLE4>  operator4,
                        Operator<TYPE4,  TYPE5,  THROWABLE5>  operator5,
                        Operator<TYPE5,  TYPE6,  THROWABLE6>  operator6,
                        Operator<TYPE6,  TYPE7,  THROWABLE7>  operator7,
                        Operator<TYPE7,  TYPE8,  THROWABLE8>  operator8,
                        Operator<TYPE8,  TYPE9,  THROWABLE9>  operator9,
                        Operator<TYPE9,  TYPE10, THROWABLE10> operator10,
                        Operator<TYPE10, TYPE11, THROWABLE11> operator11,
                        Operator<TYPE11, TYPE12, THROWABLE12> operator12,
                        Operator<TYPE12, TYPE13, THROWABLE13> operator13,
                        Operator<TYPE13, TYPE14, THROWABLE14> operator14,
                        Operator<TYPE14, TYPE15, THROWABLE15> operator15,
                        Operator<TYPE15, RESULT, THROWABLE>   operator16,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val binding = _bindingRule();
            val pipe1  = binding.operateToPipe(operator1, this);
            val pipe2  = binding.operateToPipe(operator2, pipe1);
            val pipe3  = binding.operateToPipe(operator3, pipe2);
            val pipe4  = binding.operateToPipe(operator4, pipe3);
            val pipe5  = binding.operateToPipe(operator5, pipe4);
            val pipe6  = binding.operateToPipe(operator6, pipe5);
            val pipe7  = binding.operateToPipe(operator7, pipe6);
            val pipe8  = binding.operateToPipe(operator8, pipe7);
            val pipe9  = binding.operateToPipe(operator9, pipe8);
            val pipe10 = binding.operateToPipe(operator10, pipe9);
            val pipe11 = binding.operateToPipe(operator11, pipe10);
            val pipe12 = binding.operateToPipe(operator12, pipe11);
            val pipe13 = binding.operateToPipe(operator13, pipe12);
            val pipe14 = binding.operateToPipe(operator14, pipe13);
            val pipe15 = binding.operateToPipe(operator15, pipe14);
            val result = binding.operate(operator16, pipe15);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    // OK .. if you need more ... just call another '.pipe()' :-)
    
}
