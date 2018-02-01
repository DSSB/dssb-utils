package dssb.utils.pipeable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import dssb.utils.common.UNulls;

/**
 * This interface contains often used common operators.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public interface Operators {
    
    /**
     * This operator simply call the given function.
     * 
     * @param function  the function.
     * @return  the operator for the given function.
     */
    public static <T, R, C extends Collection<R>, THROWABLE extends Throwable>
                Operator<T, R, THROWABLE> to(Function<T, R> function) {
        return t->function.apply(t);
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
    
}
