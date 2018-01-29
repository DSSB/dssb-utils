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

import java.util.stream.Stream;

import dssb.utils.common.UNulls;
import lombok.val;

// TODO - Add catch of exception.

@FunctionalInterface
public interface Pipeable<TYPE> {
    
    public TYPE _data();
    
    public default <RESULT> RESULT
        pipe(Operator<TYPE, RESULT> operator) {
            val result = operator.operateToResult(this);
            return result;
    }
    
    public default <TYPE1, RESULT>
        RESULT pipe(
            Operator<TYPE,  TYPE1>  operator1,
            Operator<TYPE1, RESULT> operator) {
        val pipe1  = operator1.operateToPipe(this);
        val result = operator .operateToResult(pipe1);
        return result;
    }
    
    public default <TYPE1, TYPE2, RESULT>
        RESULT pipe(
            Operator<TYPE,  TYPE1>  operator1,
            Operator<TYPE1, TYPE2>  operator2,
            Operator<TYPE2, RESULT> operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val result = operator .operateToResult(pipe2);
        return result;
    }
    
    public default <TYPE1, TYPE2, TYPE3, RESULT>
        RESULT pipe(
            Operator<TYPE,  TYPE1> operator1,
            Operator<TYPE1, TYPE2> operator2,
            Operator<TYPE2, TYPE3> operator3,
            Operator<TYPE3, RESULT>          operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val result = operator .operateToResult(pipe3);
        return result;
    }
    
    public default <TYPE1, TYPE2, TYPE3, TYPE4, RESULT>
        RESULT pipe(
            Operator<TYPE,  TYPE1>  operator1,
            Operator<TYPE1, TYPE2>  operator2,
            Operator<TYPE2, TYPE3>  operator3,
            Operator<TYPE3, TYPE4>  operator4,
            Operator<TYPE4, RESULT> operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val result = operator .operateToResult(pipe4);
        return result;
    }
    
    public default <TYPE1, TYPE2, TYPE3, TYPE4, TYPE5, RESULT>
        RESULT pipe(
            Operator<TYPE,  TYPE1>  operator1,
            Operator<TYPE1, TYPE2>  operator2,
            Operator<TYPE2, TYPE3>  operator3,
            Operator<TYPE3, TYPE4>  operator4,
            Operator<TYPE4, TYPE5>  operator5,
            Operator<TYPE5, RESULT> operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val pipe5  = operator5.operateToPipe(pipe4);
        val result = operator .operateToResult(pipe5);
        return result;
    }
    
}
