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

import java.util.ArrayList;
import java.util.List;

import dssb.failable.FailableException;
import dssb.utils.pipeable.binding.DefaultBinding;
import dssb.utils.pipeable.supportive.CatchNoCheckException;
import lombok.val;

// TODO -> Name and location of the operator.
// TODO -> concat

/**
 * Pipeline is a collection of operations and optional catch that acts line one operator.
 * 
 * @param <TYPE>      the type of data that this pipeline will process.
 * @param <RESULT>    the type of the pipeline result.
 * @param <THROWABLE> an exception thrown if there is a problem.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class PipeLine<TYPE, RESULT, THROWABLE extends Throwable> implements Operator<TYPE, RESULT, THROWABLE> {
    
    private BindingRule bindingRule;
    
    @SuppressWarnings("rawtypes")
    private List<Operator>           operators    = new ArrayList<>();
    private Catch<RESULT, THROWABLE> catchHandler = null;
    
    @SuppressWarnings("rawtypes")
    private PipeLine(BindingRule bindingRule, List<Operator> operators, Catch<RESULT, THROWABLE> catchHandler) {
        this.bindingRule = bindingRule;
        this.operators.addAll(operators);
        this.catchHandler = catchHandler;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public RESULT apply(TYPE value) throws THROWABLE {
        if (operators.isEmpty())
            return null;
        
        try {
            Object pipe = value;
            for (int o = 0; o < operators.size() - 1; o++) {
                val operator = operators.get(o);
                pipe = bindingRule.operateToPipe(operator, Pipe.of(pipe));
            }
            val lastOperator = operators.get(operators.size() - 1);
            val result = bindingRule.operate(lastOperator, Pipe.of(pipe));
            return (RESULT)result;
        } catch (FailableException e) {
            if (catchHandler == null)
                throw e;
            
            return (RESULT)catchHandler.handle(e);
        }
    }
    
    //== Builder ==
    
    /**
     * Create a builder.
     * 
     * @param firstOperator  the first operator.
     * @return the PipeLine builder.
     * 
     * @param <TYPE>      the type of data that this pipeline will process.
     * @param <RESULT>    the type of the pipeline result.
     * @param <THROWABLE> an exception thrown if there is a problem.
     */
    public static <TYPE, RESULT, THROWABLE extends Throwable> Builder<TYPE, RESULT, RuntimeException> startingWith(Operator<TYPE, RESULT, THROWABLE> firstOperator) {
        return new Builder<>(firstOperator);
    }
    
    /**
     * A builder for the pipeline.
     * 
     * @param <TYPE>      the type of data that this pipeline will process.
     * @param <RESULT>    the type of the pipeline result.
     * @param <THROWABLE> an exception thrown if there is a problem.
     */
    public static class Builder<TYPE, RESULT, THROWABLE extends Throwable> {
        
        private BindingRule bindingRule;
        
        @SuppressWarnings("rawtypes")
        private List<Operator> operators = new ArrayList<>();
        
        @SuppressWarnings("rawtypes")
        private Builder(Operator operator) {
            this(DefaultBinding.instance, operator);
        }
        
        @SuppressWarnings("rawtypes")
        private Builder(BindingRule bindingRule, Operator operator) {
            this.bindingRule = bindingRule;
            operators.add(operator);
        }
        
        /**
         * Add a new operation to the pipeline.
         * 
         * @param operator  the operation.
         * @return the builder with the additional operation.
         * 
         * @param <TARGET>              the data type of the target result.
         * @param <OPERATOR_THROWABLE>  the exception type of the operator.
         */
        @SuppressWarnings("unchecked")
        public <TARGET, OPERATOR_THROWABLE extends Throwable> Builder<TYPE, TARGET, RuntimeException> next(Operator<RESULT, TARGET, OPERATOR_THROWABLE> operator) {
            @SuppressWarnings("rawtypes")
            val newBuilder = new Builder(operators.get(0));
            newBuilder.operators = new ArrayList<>();
            newBuilder.operators.addAll(operators);
            newBuilder.operators.add(operator);
            return (Builder<TYPE, TARGET, RuntimeException>)newBuilder;
        }
        
        /**
         * Create the pipeline with all the operator added.
         * 
         * @return the newly created pipeline with all the operator.
         */
        public PipeLine<TYPE, RESULT, RuntimeException> build() {
            val pipeline = new PipeLine<TYPE, RESULT, RuntimeException>(bindingRule, operators, null);
            return pipeline;
        }
        
        /**
         * Build the pipeline with the catcher.
         * 
         * @param catchHandler  the catch handler
         * @return  the pipeline with the all the operator and the catch handler.
         */
        public PipeLine<TYPE, RESULT, THROWABLE> buildWith(Catch<RESULT, THROWABLE> catchHandler) {
            val pipeline = new PipeLine<TYPE, RESULT, THROWABLE>(bindingRule, operators, catchHandler);
            return pipeline;
        }
        
        /**
         * Build the pipeline with the catcher with no check exception.
         * 
         * @param catchHandler  the catch handler
         * @return  the pipeline with the all the operator and the catch handler.
         */
        public PipeLine<TYPE, RESULT, RuntimeException> buildWith(CatchNoCheckException<RESULT> catchHandler) {
            val pipeline = new PipeLine<TYPE, RESULT, RuntimeException>(bindingRule, operators, catchHandler);
            return pipeline;
        }
        
    }
    
}
