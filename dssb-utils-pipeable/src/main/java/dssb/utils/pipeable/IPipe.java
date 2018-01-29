package dssb.utils.pipeable;

public interface IPipe<TYPE extends Pipeable<TYPE>> extends Pipeable<TYPE> {
    
    public default TYPE _data() {
        return (TYPE)this;
    }
    
}
