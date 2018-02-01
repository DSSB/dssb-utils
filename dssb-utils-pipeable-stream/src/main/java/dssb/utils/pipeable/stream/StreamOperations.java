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
package dssb.utils.pipeable.stream;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import dssb.utils.pipeable.Operator;

/**
 * This interface contains often used stream operators.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public interface StreamOperations {
    
    /**
     * Map element in the stream to another value.
     * 
     * @param mapper  the mapper.
     * @return  the map operator.
     */
    public static <T, R, C extends Collection<R>> Operator<Stream<T>, Stream<R>, RuntimeException> map(Function<T, R> mapper) {
        return new Operator<Stream<T>, Stream<R>, RuntimeException>() {
            @Override
            public Stream<R> apply(Stream<T> data) {
                return data.map(mapper);
            }
        };
    }
    
    /**
     * FlatMap element in the stream to another value.
     * 
     * @param mapper  the mapper.
     * @return  the faltMap operator.
     */
    public static <T, R, C extends Collection<R>> Operator<Stream<T>, Stream<R>, RuntimeException> faltMap(Function<T, C> mapper) {
        return new Operator<Stream<T>, Stream<R>, RuntimeException>() {
            @Override
            public Stream<R> apply(Stream<T> data) {
                return data.map(mapper).flatMap(Collection::stream);
            }
        };
    }
    
    /**
     * Spread element in the stream to another value. (flatMap from List)
     * 
     * @param mapper  the mapper.
     * @return  the spread operator.
     */
    public static <T, R, C extends Collection<R>> Operator<List<T>, Stream<R>, RuntimeException> spread(Function<T, C> mapper) {
        return new Operator<List<T>, Stream<R>, RuntimeException>() {
            @Override
            public Stream<R> apply(List<T> data) {
                return data.stream().map(mapper).flatMap(Collection::stream);
            }
        };
    }
    
    /**
     * Collect the stream.
     * 
     * @param collector  the collectors.
     * @return  the collect operator.
     */
    public static<T, A, R> Operator<Stream<T>, R, RuntimeException> collect(Collector<? super T, A, R> collector) {
        return new Operator<Stream<T>, R, RuntimeException>() {
            @Override
            public R apply(Stream<T> data) {
                return data.collect(collector);
            }
        };
    }
    
    /**
     * Filter the stream.
     * 
     * @param predicate  the check if an element should be included.
     * @return  the collect operator.
     */
    public static <T, C extends Collection<T>> Operator<Stream<T>, Stream<T>, RuntimeException> filter(Predicate<T> predicate) {
        return new Operator<Stream<T>, Stream<T>, RuntimeException>() {
            @Override
            public Stream<T> apply(Stream<T> data) {
                return data.filter(predicate);
            }
        };
    }
    
    /**
     * Peek into the stream.
     * 
     * @param consumer  the consumer of the element.
     * @return  the peek operator.
     */
    public static <T, C extends Collection<T>> Operator<Stream<T>, Stream<T>, RuntimeException> peek(Consumer<T> consumer) {
        return new Operator<Stream<T>, Stream<T>, RuntimeException>() {
            @Override
            public Stream<T> apply(Stream<T> data) {
                return data.peek(consumer);
            }
        };
    }
    
    /**
     * anyMatch into the stream.
     * 
     * @param predicate  the check if an element should be included.
     * @return  the anyMatch operator.
     */
    public static <T, C extends Collection<T>> Operator<Stream<T>, Boolean, RuntimeException> anyMatch(Predicate<T> predicate) {
        return new Operator<Stream<T>, Boolean, RuntimeException>() {
            @Override
            public Boolean apply(Stream<T> data) {
                return data.anyMatch(predicate);
            }
        };
    }
    
    /**
     * allMatch into the stream.
     * 
     * @param predicate  the check if an element should be included.
     * @return  the allMatch operator.
     */
    public static <T, C extends Collection<T>> Operator<Stream<T>, Boolean, RuntimeException> allMatch(Predicate<T> predicate) {
        return new Operator<Stream<T>, Boolean, RuntimeException>() {
            @Override
            public Boolean apply(Stream<T> data) {
                return data.allMatch(predicate);
            }
        };
    }
    
}
