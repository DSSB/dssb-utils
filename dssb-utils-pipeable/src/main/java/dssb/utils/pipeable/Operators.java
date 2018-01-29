package dssb.utils.pipeable;

import java.util.Collection;
import java.util.function.Function;

import dssb.utils.common.UNulls;

public class Operators {
    
    public static <T, R, C extends Collection<R>> Operator<T, R> to(Function<T, R> mapper) {
        return t->mapper.apply(t);
    }
    
    public static <T> NullSafeOperator<T, T> or(T defaultValue) {
        return t->UNulls.or(t, defaultValue);
    }
    
}
