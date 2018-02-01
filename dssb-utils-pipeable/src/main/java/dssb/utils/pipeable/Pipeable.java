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

import dssb.failable.FailableException;
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
    
    
    //== Piping methods ==
    
    /**
     * Pipe with 1 operators.
     * 
     * @param operator1   the 1st operator.
     * @return            the result.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE, RESULT, THROWABLE> operator1) {
        val result = operator1.operateToResult(this);
        return result;
    }
    
    /**
     * Pipe with 1 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE, RESULT, THROWABLE>  operator1,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val result = operator1.operateToResult(this);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /** 
     * Pipe with 1 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   RESULT,  THROWABLE>  operator1,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val result = operator1.operateToResult(this);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 2 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @return            the result.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  RESULT,  THROWABLE>  operator2) {
        
        val pipe1  = operator1.operateToPipe(this);
        val result = operator2.operateToResult(pipe1);
        return result;
    }
    
    /**
     * Pipe with 2 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  RESULT,  THROWABLE>  operator2,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val pipe1  = operator1.operateToPipe(this);
            val result = operator2.operateToResult(pipe1);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 2 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  RESULT,  THROWABLE>  operator2,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val pipe1  = operator1.operateToPipe(this);
            val result = operator2.operateToResult(pipe1);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 3 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @return            the result.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  RESULT,  THROWABLE>  operator3) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val result = operator3.operateToResult(pipe2);
        return result;
    }
    
    /**
     * Pipe with 3 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    RESULT, THROWABLE   extends Throwable,
                    FINAL_THROWABLE extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  RESULT,  THROWABLE>  operator3,
                        Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val result = operator3.operateToResult(pipe2);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 3 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  RESULT,  THROWABLE>  operator3,
                        CatchNoCheckException<RESULT> catcher) {
        try {
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val result = operator3.operateToResult(pipe2);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 4 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @return            the result.
     **/
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
            RESULT pipe(Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
                        Operator<TYPE1,  TYPE2,  THROWABLE2>  operator2,
                        Operator<TYPE2,  TYPE3,  THROWABLE3>  operator3,
                        Operator<TYPE3,  RESULT,  THROWABLE>  operator4) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val result = operator4.operateToResult(pipe3);
        return result;
    }
    
    /**
     * Pipe with 4 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val result = operator4.operateToResult(pipe3);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 4 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val result = operator4.operateToResult(pipe3);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 5 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @return            the result.
     **/
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
        
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val result = operator5.operateToResult(pipe4);
        return result;
    }
    
    /**
     * Pipe with 5 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val result = operator5.operateToResult(pipe4);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 5 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val result = operator5.operateToResult(pipe4);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 6 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @return            the result.
     **/
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
        
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val pipe5  = operator5.operateToPipe(pipe4);
        val result = operator6.operateToResult(pipe5);
        return result;
    }
    
    /**
     * Pipe with 6 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val result = operator6.operateToResult(pipe5);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 6 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val result = operator6.operateToResult(pipe5);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 7 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @return            the result.
     **/
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
        
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val pipe5  = operator5.operateToPipe(pipe4);
        val pipe6  = operator6.operateToPipe(pipe5);
        val result = operator7.operateToResult(pipe6);
        return result;
    }
    
    /**
     * Pipe with 7 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val result = operator7.operateToResult(pipe6);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 7 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val result = operator7.operateToResult(pipe6);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 8 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @return            the result.
     **/
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
        
        val pipe1 = operator1.operateToPipe(this);
        val pipe2 = operator2.operateToPipe(pipe1);
        val pipe3 = operator3.operateToPipe(pipe2);
        val pipe4 = operator4.operateToPipe(pipe3);
        val pipe5 = operator5.operateToPipe(pipe4);
        val pipe6 = operator6.operateToPipe(pipe5);
        val pipe7 = operator7.operateToPipe(pipe6);
        val result = operator8.operateToResult(pipe7);
        return result;
    }
    
    /**
     * Pipe with 8 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val result = operator8.operateToResult(pipe7);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 8 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val result = operator8.operateToResult(pipe7);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 9 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @return            the result.
     **/
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
        val pipe1 = operator1.operateToPipe(this);
        val pipe2 = operator2.operateToPipe(pipe1);
        val pipe3 = operator3.operateToPipe(pipe2);
        val pipe4 = operator4.operateToPipe(pipe3);
        val pipe5 = operator5.operateToPipe(pipe4);
        val pipe6 = operator6.operateToPipe(pipe5);
        val pipe7 = operator7.operateToPipe(pipe6);
        val pipe8 = operator8.operateToPipe(pipe7);
        val result = operator9.operateToResult(pipe8);
        return result;
    }
    
    /**
     * Pipe with 9 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val pipe8 = operator8.operateToPipe(pipe7);
            val result = operator9.operateToResult(pipe8);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 9 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val pipe8 = operator8.operateToPipe(pipe7);
            val result = operator9.operateToResult(pipe8);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 10 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @return            the result.
     **/
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
        val pipe1 = operator1.operateToPipe(this);
        val pipe2 = operator2.operateToPipe(pipe1);
        val pipe3 = operator3.operateToPipe(pipe2);
        val pipe4 = operator4.operateToPipe(pipe3);
        val pipe5 = operator5.operateToPipe(pipe4);
        val pipe6 = operator6.operateToPipe(pipe5);
        val pipe7 = operator7.operateToPipe(pipe6);
        val pipe8 = operator8.operateToPipe(pipe7);
        val pipe9 = operator9.operateToPipe(pipe8);
        val result = operator10.operateToResult(pipe9);
        return result;
    }
    
    /**
     * Pipe with 10 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val pipe8 = operator8.operateToPipe(pipe7);
            val pipe9 = operator9.operateToPipe(pipe8);
            val result = operator10.operateToResult(pipe9);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 10 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val pipe8 = operator8.operateToPipe(pipe7);
            val pipe9 = operator9.operateToPipe(pipe8);
            val result = operator10.operateToResult(pipe9);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 11 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @return            the result.
     **/
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
        val pipe1 = operator1.operateToPipe(this);
        val pipe2 = operator2.operateToPipe(pipe1);
        val pipe3 = operator3.operateToPipe(pipe2);
        val pipe4 = operator4.operateToPipe(pipe3);
        val pipe5 = operator5.operateToPipe(pipe4);
        val pipe6 = operator6.operateToPipe(pipe5);
        val pipe7 = operator7.operateToPipe(pipe6);
        val pipe8 = operator8.operateToPipe(pipe7);
        val pipe9 = operator9.operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val result = operator11.operateToResult(pipe10);
        return result;
    }
    
    /**
     * Pipe with 11 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val pipe8 = operator8.operateToPipe(pipe7);
            val pipe9 = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val result = operator11.operateToResult(pipe10);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 11 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val pipe8 = operator8.operateToPipe(pipe7);
            val pipe9 = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val result = operator11.operateToResult(pipe10);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 12 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @return            the result.
     **/
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
        val pipe1 = operator1.operateToPipe(this);
        val pipe2 = operator2.operateToPipe(pipe1);
        val pipe3 = operator3.operateToPipe(pipe2);
        val pipe4 = operator4.operateToPipe(pipe3);
        val pipe5 = operator5.operateToPipe(pipe4);
        val pipe6 = operator6.operateToPipe(pipe5);
        val pipe7 = operator7.operateToPipe(pipe6);
        val pipe8 = operator8.operateToPipe(pipe7);
        val pipe9 = operator9.operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val result = operator12.operateToResult(pipe11);
        return result;
    }
    
    /**
     * Pipe with 12 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val pipe8 = operator8.operateToPipe(pipe7);
            val pipe9 = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val result = operator12.operateToResult(pipe11);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 12 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1 = operator1.operateToPipe(this);
            val pipe2 = operator2.operateToPipe(pipe1);
            val pipe3 = operator3.operateToPipe(pipe2);
            val pipe4 = operator4.operateToPipe(pipe3);
            val pipe5 = operator5.operateToPipe(pipe4);
            val pipe6 = operator6.operateToPipe(pipe5);
            val pipe7 = operator7.operateToPipe(pipe6);
            val pipe8 = operator8.operateToPipe(pipe7);
            val pipe9 = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val result = operator12.operateToResult(pipe11);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 13 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @return            the result.
     **/
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
        val pipe1 = operator1.operateToPipe(this);
        val pipe2 = operator2.operateToPipe(pipe1);
        val pipe3 = operator3.operateToPipe(pipe2);
        val pipe4 = operator4.operateToPipe(pipe3);
        val pipe5 = operator5.operateToPipe(pipe4);
        val pipe6 = operator6.operateToPipe(pipe5);
        val pipe7 = operator7.operateToPipe(pipe6);
        val pipe8 = operator8.operateToPipe(pipe7);
        val pipe9 = operator9.operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val result = operator13.operateToResult(pipe12);
        return result;
    }
    
    /**
     * Pipe with 13 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val pipe7  = operator7.operateToPipe(pipe6);
            val pipe8  = operator8.operateToPipe(pipe7);
            val pipe9  = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val pipe12 = operator12.operateToPipe(pipe11);
            val result = operator13.operateToResult(pipe12);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 13 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val pipe7  = operator7.operateToPipe(pipe6);
            val pipe8  = operator8.operateToPipe(pipe7);
            val pipe9  = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val pipe12 = operator12.operateToPipe(pipe11);
            val result = operator13.operateToResult(pipe12);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 14 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @return            the result.
     **/
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
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val pipe5  = operator5.operateToPipe(pipe4);
        val pipe6  = operator6.operateToPipe(pipe5);
        val pipe7  = operator7.operateToPipe(pipe6);
        val pipe8  = operator8.operateToPipe(pipe7);
        val pipe9  = operator9.operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val pipe13 = operator13.operateToPipe(pipe12);
        val result = operator14.operateToResult(pipe13);
        return result;
    }
    
    /**
     * Pipe with 14 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val pipe7  = operator7.operateToPipe(pipe6);
            val pipe8  = operator8.operateToPipe(pipe7);
            val pipe9  = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val pipe12 = operator12.operateToPipe(pipe11);
            val pipe13 = operator13.operateToPipe(pipe12);
            val result = operator14.operateToResult(pipe13);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 14 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val pipe7  = operator7.operateToPipe(pipe6);
            val pipe8  = operator8.operateToPipe(pipe7);
            val pipe9  = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val pipe12 = operator12.operateToPipe(pipe11);
            val pipe13 = operator13.operateToPipe(pipe12);
            val result = operator14.operateToResult(pipe13);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 15 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @param operator15  the 15th operator.
     * @return            the result.
     **/
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
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val pipe5  = operator5.operateToPipe(pipe4);
        val pipe6  = operator6.operateToPipe(pipe5);
        val pipe7  = operator7.operateToPipe(pipe6);
        val pipe8  = operator8.operateToPipe(pipe7);
        val pipe9  = operator9.operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val pipe13 = operator13.operateToPipe(pipe12);
        val pipe14 = operator14.operateToPipe(pipe13);
        val result = operator15.operateToResult(pipe14);
        return result;
    }
    
    /**
     * Pipe with 15 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @param operator15  the 15th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val pipe7  = operator7.operateToPipe(pipe6);
            val pipe8  = operator8.operateToPipe(pipe7);
            val pipe9  = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val pipe12 = operator12.operateToPipe(pipe11);
            val pipe13 = operator13.operateToPipe(pipe12);
            val pipe14 = operator14.operateToPipe(pipe13);
            val result = operator15.operateToResult(pipe14);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 15 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @param operator15  the 15th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val pipe7  = operator7.operateToPipe(pipe6);
            val pipe8  = operator8.operateToPipe(pipe7);
            val pipe9  = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val pipe12 = operator12.operateToPipe(pipe11);
            val pipe13 = operator13.operateToPipe(pipe12);
            val pipe14 = operator14.operateToPipe(pipe13);
            val result = operator15.operateToResult(pipe14);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 16 operators.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @param operator15  the 15th operator.
     * @param operator16  the 16th operator.
     * @return            the result.
     **/
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
        
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val pipe5  = operator5.operateToPipe(pipe4);
        val pipe6  = operator6.operateToPipe(pipe5);
        val pipe7  = operator7.operateToPipe(pipe6);
        val pipe8  = operator8.operateToPipe(pipe7);
        val pipe9  = operator9.operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val pipe13 = operator13.operateToPipe(pipe12);
        val pipe14 = operator14.operateToPipe(pipe13);
        val pipe15 = operator15.operateToPipe(pipe14);
        val result = operator16.operateToResult(pipe15);
        return result;
    }
    
    /**
     * Pipe with 16 operators and a catcher that might throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @param operator15  the 15th operator.
     * @param operator16  the 16th operator.
     * @param catcher     the catcher.
     * @return            the result.
     * @throws FINAL_THROWABLE if the catcher throw an exception.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val pipe7  = operator7.operateToPipe(pipe6);
            val pipe8  = operator8.operateToPipe(pipe7);
            val pipe9  = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val pipe12 = operator12.operateToPipe(pipe11);
            val pipe13 = operator13.operateToPipe(pipe12);
            val pipe14 = operator14.operateToPipe(pipe13);
            val pipe15 = operator15.operateToPipe(pipe14);
            val result = operator16.operateToResult(pipe15);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    /**
     * Pipe with 16 operators and a catcher that does not throw a checked exception.
     * 
     * @param operator1   the 1st operator.
     * @param operator2   the 2nd operator.
     * @param operator3   the 3rd operator.
     * @param operator4   the 4th operator.
     * @param operator5   the 5th operator.
     * @param operator6   the 6th operator.
     * @param operator7   the 7th operator.
     * @param operator8   the 8th operator.
     * @param operator9   the 9th operator.
     * @param operator10  the 10th operator.
     * @param operator11  the 11th operator.
     * @param operator12  the 12th operator.
     * @param operator13  the 13th operator.
     * @param operator14  the 14th operator.
     * @param operator15  the 15th operator.
     * @param operator16  the 16th operator.
     * @param catcher     the catcher.
     * @return            the result.
     **/
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
            val pipe1  = operator1.operateToPipe(this);
            val pipe2  = operator2.operateToPipe(pipe1);
            val pipe3  = operator3.operateToPipe(pipe2);
            val pipe4  = operator4.operateToPipe(pipe3);
            val pipe5  = operator5.operateToPipe(pipe4);
            val pipe6  = operator6.operateToPipe(pipe5);
            val pipe7  = operator7.operateToPipe(pipe6);
            val pipe8  = operator8.operateToPipe(pipe7);
            val pipe9  = operator9.operateToPipe(pipe8);
            val pipe10 = operator10.operateToPipe(pipe9);
            val pipe11 = operator11.operateToPipe(pipe10);
            val pipe12 = operator12.operateToPipe(pipe11);
            val pipe13 = operator13.operateToPipe(pipe12);
            val pipe14 = operator14.operateToPipe(pipe13);
            val pipe15 = operator15.operateToPipe(pipe14);
            val result = operator16.operateToResult(pipe15);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    
    // OK .. if you need more ... just call another '.pipe()' :-)
    
}
