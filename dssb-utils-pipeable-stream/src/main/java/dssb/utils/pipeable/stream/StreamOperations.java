package dssb.utils.pipeable.stream;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import dssb.utils.pipeable.Operator;

public class StreamOperations {
    
    public static <T, R, C extends Collection<R>> Operator<Stream<T>, Stream<R>> map(Function<T, R> mapper) {
        return new Operator<Stream<T>, Stream<R>>() {
            @Override
            public Stream<R> apply(Stream<T> data) {
                return data.map(mapper);
            }
        };
    }
    
    public static <T, R, C extends Collection<R>> Operator<Stream<T>, Stream<R>> faltMap(Function<T, C> mapper) {
        return new Operator<Stream<T>, Stream<R>>() {
            @Override
            public Stream<R> apply(Stream<T> data) {
                return data.map(mapper).flatMap(Collection::stream);
            }
        };
    }
    
    public static <T, R, C extends Collection<R>> Operator<List<T>, Stream<R>> spread(Function<T, C> mapper) {
        return new Operator<List<T>, Stream<R>>() {
            @Override
            public Stream<R> apply(List<T> data) {
                return data.stream().map(mapper).flatMap(Collection::stream);
            }
        };
    }
    
    public static<T, A, R> Operator<Stream<T>, R> collect(Collector<? super T, A, R> collector) {
        return new Operator<Stream<T>, R>() {
            @Override
            public R apply(Stream<T> data) {
                return data.collect(collector);
            }
        };
    }
    
    public static <T, C extends Collection<T>> Operator<Stream<T>, Stream<T>> where(Predicate<T> predicate) {
        return new Operator<Stream<T>, Stream<T>>() {
            @Override
            public Stream<T> apply(Stream<T> data) {
                return data.filter(predicate);
            }
        };
    }
    
    public static <T, C extends Collection<T>> Operator<Stream<T>, Stream<T>> peek(Consumer<T> consumer) {
        return new Operator<Stream<T>, Stream<T>>() {
            @Override
            public Stream<T> apply(Stream<T> data) {
                return data.peek(consumer);
            }
        };
    }
    
    public static <T, C extends Collection<T>> Operator<Stream<T>, Boolean> containOneThat(Predicate<T> predicate) {
        return new Operator<Stream<T>, Boolean>() {
            @Override
            public Boolean apply(Stream<T> data) {
                return data.anyMatch(predicate);
            }
        };
    }
    
}
