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

// TODO - Add catch of exception.

@FunctionalInterface
public interface Pipeable<TYPE> {
    
    public TYPE _data();
    
    public default <RESULT, THROWABLE extends Throwable> RESULT
        pipe(Operator<TYPE, RESULT, THROWABLE> operator) {
        val result = operator.operateToResult(this);
        return result;
    }
    public default <RESULT, THROWABLE extends Throwable, FINAL_THROWABLE extends Throwable> RESULT
        pipe(Operator<TYPE, RESULT, THROWABLE> operator,
             Catch<RESULT, FINAL_THROWABLE> catcher) throws FINAL_THROWABLE {
        try {
            val result = operator.operateToResult(this);
            return result;
        } catch (FailableException failableException) {
            return catcher.handle(failableException);
        }
    }
    public default <RESULT, THROWABLE extends Throwable> RESULT
    pipe(Operator<TYPE, RESULT, THROWABLE> operator,
         CatchWithOr<RESULT> catcher) {
    try {
        val result = operator.operateToResult(this);
        return result;
    } catch (FailableException failableException) {
        return catcher.handle(failableException);
    }
}
    
    public default <TYPE1,  THROWABLE1 extends Throwable,
                    RESULT, THROWABLE extends Throwable>
        RESULT pipe(
            Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
            Operator<TYPE1, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1.operateToPipe(this);
        val result = operator .operateToResult(pipe1);
        return result;
    }
    
    public default <TYPE1,  THROWABLE1 extends Throwable,
                    TYPE2,  THROWABLE2 extends Throwable,
                    RESULT, THROWABLE  extends Throwable>
        RESULT pipe(
            Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
            Operator<TYPE1, TYPE2,  THROWABLE2> operator2,
            Operator<TYPE2, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val result = operator .operateToResult(pipe2);
        return result;
    }
    
    public default <TYPE1,  THROWABLE1 extends Throwable,
                    TYPE2,  THROWABLE2 extends Throwable,
                    TYPE3,  THROWABLE3 extends Throwable,
                    RESULT, THROWABLE  extends Throwable>
        RESULT pipe(
            Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
            Operator<TYPE1, TYPE2,  THROWABLE2> operator2,
            Operator<TYPE2, TYPE3,  THROWABLE3> operator3,
            Operator<TYPE3, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val result = operator .operateToResult(pipe3);
        return result;
    }
    
    public default <TYPE1,  THROWABLE1 extends Throwable,
                    TYPE2,  THROWABLE2 extends Throwable,
                    TYPE3,  THROWABLE3 extends Throwable,
                    TYPE4,  THROWABLE4 extends Throwable,
                    RESULT, THROWABLE  extends Throwable>
        RESULT pipe(
                Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
                Operator<TYPE1, TYPE2,  THROWABLE2> operator2,
                Operator<TYPE2, TYPE3,  THROWABLE3> operator3,
                Operator<TYPE3, TYPE4,  THROWABLE4> operator4,
                Operator<TYPE4, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val result = operator .operateToResult(pipe4);
        return result;
    }
    
    public default <TYPE1,  THROWABLE1 extends Throwable,
                    TYPE2,  THROWABLE2 extends Throwable,
                    TYPE3,  THROWABLE3 extends Throwable,
                    TYPE4,  THROWABLE4 extends Throwable,
                    TYPE5,  THROWABLE5 extends Throwable,
                    RESULT, THROWABLE  extends Throwable>
        RESULT pipe(
                Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
                Operator<TYPE1, TYPE2,  THROWABLE2> operator2,
                Operator<TYPE2, TYPE3,  THROWABLE3> operator3,
                Operator<TYPE3, TYPE4,  THROWABLE4> operator4,
                Operator<TYPE4, TYPE5,  THROWABLE5> operator5,
                Operator<TYPE5, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val pipe5  = operator5.operateToPipe(pipe4);
        val result = operator .operateToResult(pipe5);
        return result;
    }
    
    public default <TYPE1,  THROWABLE1 extends Throwable,
                    TYPE2,  THROWABLE2 extends Throwable,
                    TYPE3,  THROWABLE3 extends Throwable,
                    TYPE4,  THROWABLE4 extends Throwable,
                    TYPE5,  THROWABLE5 extends Throwable,
                    TYPE6,  THROWABLE6 extends Throwable,
                    RESULT, THROWABLE  extends Throwable>
        RESULT pipe(
                Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
                Operator<TYPE1, TYPE2,  THROWABLE2> operator2,
                Operator<TYPE2, TYPE3,  THROWABLE3> operator3,
                Operator<TYPE3, TYPE4,  THROWABLE4> operator4,
                Operator<TYPE4, TYPE5,  THROWABLE5> operator5,
                Operator<TYPE5, TYPE6,  THROWABLE6> operator6,
                Operator<TYPE6, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1.operateToPipe(this);
        val pipe2  = operator2.operateToPipe(pipe1);
        val pipe3  = operator3.operateToPipe(pipe2);
        val pipe4  = operator4.operateToPipe(pipe3);
        val pipe5  = operator5.operateToPipe(pipe4);
        val pipe6  = operator6.operateToPipe(pipe5);
        val result = operator .operateToResult(pipe6);
        return result;
    }
    

    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
        RESULT pipe(
                Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
                Operator<TYPE1, TYPE2,  THROWABLE2> operator2,
                Operator<TYPE2, TYPE3,  THROWABLE3> operator3,
                Operator<TYPE3, TYPE4,  THROWABLE4> operator4,
                Operator<TYPE4, TYPE5,  THROWABLE5> operator5,
                Operator<TYPE5, TYPE6,  THROWABLE6> operator6,
                Operator<TYPE6, TYPE7,  THROWABLE7> operator7,
                Operator<TYPE7, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val result = operator  .operateToResult(pipe7);
        return result;
    }
    
    public default <TYPE1,  THROWABLE1  extends Throwable,
                    TYPE2,  THROWABLE2  extends Throwable,
                    TYPE3,  THROWABLE3  extends Throwable,
                    TYPE4,  THROWABLE4  extends Throwable,
                    TYPE5,  THROWABLE5  extends Throwable,
                    TYPE6,  THROWABLE6  extends Throwable,
                    TYPE7,  THROWABLE7  extends Throwable,
                    TYPE8,  THROWABLE8  extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
        RESULT pipe(
                Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
                Operator<TYPE1, TYPE2,  THROWABLE2> operator2,
                Operator<TYPE2, TYPE3,  THROWABLE3> operator3,
                Operator<TYPE3, TYPE4,  THROWABLE4> operator4,
                Operator<TYPE4, TYPE5,  THROWABLE5> operator5,
                Operator<TYPE5, TYPE6,  THROWABLE6> operator6,
                Operator<TYPE6, TYPE7,  THROWABLE7> operator7,
                Operator<TYPE7, TYPE8,  THROWABLE8> operator8,
                Operator<TYPE8, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val pipe8  = operator8 .operateToPipe(pipe7);
        val result = operator  .operateToResult(pipe8);
        return result;
    }
    
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
        RESULT pipe(
                Operator<TYPE,  TYPE1,  THROWABLE1> operator1,
                Operator<TYPE1, TYPE2,  THROWABLE2> operator2,
                Operator<TYPE2, TYPE3,  THROWABLE3> operator3,
                Operator<TYPE3, TYPE4,  THROWABLE4> operator4,
                Operator<TYPE4, TYPE5,  THROWABLE5> operator5,
                Operator<TYPE5, TYPE6,  THROWABLE6> operator6,
                Operator<TYPE6, TYPE7,  THROWABLE7> operator7,
                Operator<TYPE7, TYPE8,  THROWABLE8> operator8,
                Operator<TYPE8, TYPE9,  THROWABLE9> operator9,
                Operator<TYPE9, RESULT, THROWABLE>  operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val pipe8  = operator8 .operateToPipe(pipe7);
        val pipe9  = operator9 .operateToPipe(pipe8);
        val result = operator  .operateToResult(pipe9);
        return result;
    }
    
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
        RESULT pipe(
                Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
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
                Operator<TYPE11, RESULT, THROWABLE>   operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val pipe8  = operator8 .operateToPipe(pipe7);
        val pipe9  = operator9 .operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val result = operator  .operateToResult(pipe11);
        return result;
    }
    
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
        RESULT pipe(
                Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
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
                Operator<TYPE12, RESULT, THROWABLE>   operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val pipe8  = operator8 .operateToPipe(pipe7);
        val pipe9  = operator9 .operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val result = operator  .operateToResult(pipe12);
        return result;
    }
    
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
        RESULT pipe(
                Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
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
                Operator<TYPE13, RESULT, THROWABLE>   operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val pipe8  = operator8 .operateToPipe(pipe7);
        val pipe9  = operator9 .operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val pipe13 = operator13.operateToPipe(pipe12);
        val result = operator  .operateToResult(pipe13);
        return result;
    }
    
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
        RESULT pipe(
                Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
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
                Operator<TYPE14, RESULT, THROWABLE>   operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val pipe8  = operator8 .operateToPipe(pipe7);
        val pipe9  = operator9 .operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val pipe13 = operator13.operateToPipe(pipe12);
        val pipe14 = operator14.operateToPipe(pipe13);
        val result = operator  .operateToResult(pipe14);
        return result;
    }
    
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
        RESULT pipe(
                Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
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
                Operator<TYPE15, RESULT, THROWABLE>   operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val pipe8  = operator8 .operateToPipe(pipe7);
        val pipe9  = operator9 .operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val pipe13 = operator13.operateToPipe(pipe12);
        val pipe14 = operator14.operateToPipe(pipe13);
        val pipe15 = operator15.operateToPipe(pipe14);
        val result = operator  .operateToResult(pipe15);
        return result;
    }
    
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
                    TYPE16, THROWABLE16 extends Throwable,
                    RESULT, THROWABLE   extends Throwable>
        RESULT pipe(
                Operator<TYPE,   TYPE1,  THROWABLE1>  operator1,
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
                Operator<TYPE15, TYPE16, THROWABLE16> operator16,
                Operator<TYPE16, RESULT, THROWABLE>   operator) {
        val pipe1  = operator1 .operateToPipe(this);
        val pipe2  = operator2 .operateToPipe(pipe1);
        val pipe3  = operator3 .operateToPipe(pipe2);
        val pipe4  = operator4 .operateToPipe(pipe3);
        val pipe5  = operator5 .operateToPipe(pipe4);
        val pipe6  = operator6 .operateToPipe(pipe5);
        val pipe7  = operator7 .operateToPipe(pipe6);
        val pipe8  = operator8 .operateToPipe(pipe7);
        val pipe9  = operator9 .operateToPipe(pipe8);
        val pipe10 = operator10.operateToPipe(pipe9);
        val pipe11 = operator11.operateToPipe(pipe10);
        val pipe12 = operator12.operateToPipe(pipe11);
        val pipe13 = operator13.operateToPipe(pipe12);
        val pipe14 = operator14.operateToPipe(pipe13);
        val pipe15 = operator15.operateToPipe(pipe14);
        val pipe16 = operator16.operateToPipe(pipe15);
        val result = operator  .operateToResult(pipe16);
        return result;
    }
    
    // OK .. if you need more ... just call another '.pipe()' :-)
    
}
