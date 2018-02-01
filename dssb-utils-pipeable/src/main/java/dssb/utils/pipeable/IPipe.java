package dssb.utils.pipeable;

/**
 * Classes implementing this interface can pipe.
 * 
 * This interface extends Pipeable and return itself as the wrapped data.
 * This will allow any class to easily implements Pipeable by simply implement this interface.
 * Make sure that the TYPE is the class that implement this.
 * 
 * @param <TYPE> the type of data that this pipeable is holding.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public interface IPipe<TYPE extends Pipeable<TYPE>> extends Pipeable<TYPE> {
    
    @SuppressWarnings("unchecked")
    public default TYPE _data() {
        return (TYPE)this;
    }
    
}
