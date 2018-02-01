package dssb.utils.pipeable;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.function.Function;
import java.util.function.Supplier;

import dssb.failable.Failable;
import dssb.failable.FailableException;
import lombok.val;

public class Catch<RESULT, THROWABLE extends Throwable> {
    
    private Failable.Function<FailableException, RESULT, THROWABLE> handler;
    
    public Catch(Failable.Function<FailableException, RESULT, THROWABLE> handler) {
        this.handler = handler;
    }
    
    public RESULT handle(FailableException exception) throws THROWABLE {
        if (this.handler == null)
            return null;
        
        return this.handler.apply(exception);
    }
    
    public static <RESULT, THROWABLE extends Throwable> Catch then(Failable.Function<FailableException, RESULT, THROWABLE> handler) {
        return new Catch<RESULT, THROWABLE>(handler);
    }
    
    public static <RESULT> CatchNoCheckException<RESULT> thenReturn(RESULT orValue) {
        return new CatchNoCheckException<>(e->orValue);
    }
    
    public static <RESULT> CatchNoCheckException<RESULT> thenReturn(Supplier<RESULT> orSupplier) {
        return new CatchNoCheckException<>(e->((orSupplier != null) ? orSupplier.get() : null));
    }
    
    public static <RESULT> CatchNoCheckException<RESULT> thenReturn(Function<FailableException, RESULT> orFunction) {
        return new CatchNoCheckException<>(orFunction);
    }
    
    public static <RESULT, THROWABLE extends Throwable> Catch<RESULT, THROWABLE> thenThrow() throws THROWABLE {
        return new Catch<RESULT, THROWABLE>(exception -> {
            @SuppressWarnings("unchecked")
            val cause = (THROWABLE)exception.getCause();
            throw cause;
        });
    }
    public static <RESULT> CatchNoCheckException thenIgnore() {
        return new CatchNoCheckException<RESULT>(null);
    }
    
    public static <RESULT, THROWABLE extends Throwable> CatchNoCheckException thenPrintStackTrace() {
        return new CatchNoCheckException<RESULT>(e->{
            e.printStackTrace();
            return null;
        });
    }
    
    public static <RESULT, THROWABLE extends Throwable> CatchNoCheckException thenPrintStackTrace(PrintStream ps) {
        return new CatchNoCheckException<RESULT>(e->{
            e.printStackTrace(ps);
            return null;
        });
    }
    
    public static <RESULT, THROWABLE extends Throwable> CatchNoCheckException thenPrintStackTrace(PrintWriter pw) {
        return new CatchNoCheckException<RESULT>(e->{
            e.printStackTrace(pw);
            return null;
        });
    }
    
}
