package dssb.utils.pipeable;

@FunctionalInterface
public interface NullSafeOperator<TYPE, RESULT, THROWABLE extends Throwable> extends Operator<TYPE, RESULT, THROWABLE> {
    
}