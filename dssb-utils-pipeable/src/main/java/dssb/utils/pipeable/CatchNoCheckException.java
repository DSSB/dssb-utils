package dssb.utils.pipeable;

import dssb.failable.Failable;
import dssb.failable.Failable.Function;
import dssb.failable.FailableException;

@SuppressWarnings("rawtypes")
public class CatchNoCheckException<RESULT> extends Catch<RESULT, RuntimeException> {
    
    private java.util.function.Function orFunction;
    
    public <R extends RESULT, THROWABLE extends Throwable> CatchNoCheckException(java.util.function.Function<FailableException, R> orFunction) {
        super(null);
        this.orFunction = orFunction;
    }
    
    @SuppressWarnings("unchecked")
    public RESULT handle(FailableException exception) {
        if (this.orFunction == null)
            return null;
        
        return (RESULT)this.orFunction.apply(exception);
    }
    
}
