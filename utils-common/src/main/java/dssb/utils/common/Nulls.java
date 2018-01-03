//  ========================================================================
//  Copyright (c) 2017 Nawapunth Manusitthipol.
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
package dssb.utils.common;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

/**
 * This utility class contains useful methods involving null.
 * 
 * @author NawaMan
 */
@UtilityClass
public class Nulls {
    
    /**
     * Returns {@code true} if theGivenObject is null. 
     * 
     * @param theGivenObject the given object.
     * @return {@code true} if theGivenObject is null.
     **/
    public boolean isNull(Object theGivenObject) {
        return (theGivenObject == null);
    }
    
    /**
     * Returns {@code true} if theGivenObject is not null. 
     * 
     * @param theGivenObject  the given object.
     * @return {@code true} if theGivenObject is not null.
     **/
    public boolean isNotNull(Object theGivenObject) {
        return (theGivenObject != null);
    }
    
    /**
     * Returns elseValue if theGivenObject is null. 
     * 
     * @param  theGivenObject  the given object.
     * @param  elseValue       the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     * @param  <T>  the data type of the given object.
     **/
    public <T> T or(T theGivenObject, T elseValue) {
        return (theGivenObject == null) ? elseValue : theGivenObject;
    }
    
    /**
     * Returns the result of elseSupplier if the given object is null. 
     * 
     * @param theGivenObject  the given object.
     * @param elseSupplier    the supplier for when the given object is null.
     * @return theGivenObject if not null or value from the elseSupplier if null.
     * @param  <T>  the data type of the given object.
     **/
    public <T> T orGet(T theGivenObject, Supplier<? extends T> elseSupplier) {
        return (theGivenObject == null) ? elseSupplier.get() : theGivenObject;
    }
    
    /**
     * Extension method to create optional of theGivenObject. 
     * 
     * @param theGivenObject  the given object.
     * @return the optional value of the theGivenObject.
     * @param  <T>  the data type of the given object.
     **/
    public <T> Optional<T> whenNotNull(T theGivenObject) {
        return Optional.ofNullable(theGivenObject);
    }
    
    /**
     * Extension method to create optional of theGivenObject. 
     * 
     * @param theGivenObject  the given object.
     * @return the optional value of the theGivenObject.
     * @param  <T>  the data type of the given object.
     **/
    public <T> Optional<T> toOptional(T theGivenObject) {
        return Optional.ofNullable(theGivenObject);
    }
    
    /**
     * Return the given object if the test yields {@code true} or else return null.
     * 
     * @param theGivenObject  the given object.
     * @param theTest         the test.
     * @return  the original object or null.
     * @param  <T>  the data type of the given object.
     */
    public <T> T when(T theGivenObject, Predicate<T> theTest) {
        if (isNull(theGivenObject))
            return null;
        if (isNull(theTest))
            return null;
        if (theTest.test(theGivenObject))
            return theGivenObject;
        return null;
    }
    
    /**
     * Return the given object if it is of the given class or else return null.
     * 
     * @param theGivenObject  the given object.
     * @param theClass        the class.
     * @return  the original object as the type class or null.
     * @param  <T>  the data type of the given object.
     * @param  <S>  the data type of the returned object.
     */
    public <T, S> S as(T theGivenObject, Class<S> theClass) {
        if (theClass.isInstance(theGivenObject))
            return theClass.cast(theGivenObject);
        return null;
    }
    
    /**
     * Map the given object using the transformation if the given object is not null or else return null.
     * 
     * @param theGivenObject  the given object.
     * @param transformation  the transformation function.
     * @return  the transformed value.
     * @param  <F>  the data type of the given object.
     * @param  <T>  the data type of the target object.
     */
    public <F, T> T mapTo(F theGivenObject, Function<F, T> transformation) {
        return (theGivenObject != null) ? transformation.apply(theGivenObject) : null;
    }
    
    //== Primitive types 'or' ==
    
    /**
     * Returns elseValue if theGivenInteger is null. 
     * 
     * @param  theGivenInteger  the given integer.
     * @param  elseValue        the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public int or(Integer theGivenInteger, int elseValue) {
        return (theGivenInteger == null) ? elseValue : theGivenInteger.intValue();
    }
    
    /**
     * Returns elseValue if theGivenLong is null. 
     * 
     * @param  theGivenLong  the given long.
     * @param  elseValue     the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public long or(Long theGivenLong, long elseValue) {
        return (theGivenLong == null) ? elseValue : theGivenLong.longValue();
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenDouble  the given double.
     * @param  elseValue       the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public double or(Double theGivenDouble, double elseValue) {
        return (theGivenDouble == null) ? elseValue : theGivenDouble.doubleValue();
    }
    
}
