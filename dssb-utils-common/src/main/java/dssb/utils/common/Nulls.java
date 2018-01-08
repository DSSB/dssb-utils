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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;
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
     * @param  theGivenObject the given object.
     * @return {@code true} if theGivenObject is null.
     **/
    public boolean isNull(Object theGivenObject) {
        return (theGivenObject == null);
    }
    
    /**
     * Returns {@code true} if theGivenObject is not null. 
     * 
     * @param  theGivenObject  the given object.
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
     * @param  <OBJECT>        the data type of the given object.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public <OBJECT> OBJECT or(OBJECT theGivenObject, OBJECT elseValue) {
        return (theGivenObject == null) ? elseValue : theGivenObject;
    }
    
    /**
     * Returns the result of elseSupplier if the given object is null. 
     * 
     * @param  theGivenObject  the given object.
     * @param  elseSupplier    the supplier for when the given object is null.
     * @param  <OBJECT>        the data type of the given object.
     * @return theGivenObject if not null or value from the elseSupplier if null.
     **/
    public <OBJECT> OBJECT orGet(OBJECT theGivenObject, Supplier<? extends OBJECT> elseSupplier) {
        return (theGivenObject == null) ? elseSupplier.get() : theGivenObject;
    }
    
    /**
     * Extension method to create optional of theGivenObject. 
     * 
     * @param  theGivenObject  the given object.
     * @param  <OBJECT>        the data type of the given object.
     * @return the optional value of the theGivenObject.
     **/
    public <OBJECT> Optional<OBJECT> toOptional(OBJECT theGivenObject) {
        return Optional.ofNullable(theGivenObject);
    }
    
    /**
     * Extension method to create optional of theGivenObject. 
     * 
     * @param  theGivenObject  the given object.
     * @param  <OBJECT>        the data type of the given object.
     * @return the optional value of the theGivenObject.
     **/
    public <OBJECT> Optional<OBJECT> whenNotNull(OBJECT theGivenObject) {
        return Optional.ofNullable(theGivenObject);
    }
    
    /**
     * Perform the theAction of theGivenObject is not null. 
     * 
     * @param  theGivenObject  the given object.
     * @param  theAction       the action.
     * @param  <OBJECT>        the data type of the given object.
     **/
    public <OBJECT> void whenNotNull(OBJECT theGivenObject, Consumer<OBJECT> theAction) {
        if (theGivenObject == null)
            return;
        theAction.accept(theGivenObject);
    }
    
    /**
     * Return the given object if the test yields {@code true} or else return null.
     * 
     * @param  theGivenObject  the given object.
     * @param  theTest         the test.
     * @param  <OBJECT>        the data type of the given object.
     * @return  the original object or null.
     */
    public <OBJECT> OBJECT when(OBJECT theGivenObject, Predicate<OBJECT> theTest) {
        if (theGivenObject == null)
            return null;
        if (theTest.test(theGivenObject))
            return theGivenObject;
        return null;
    }
    
    /**
     * Return the given object if it is of the given class or else return null.
     * 
     * @param  theGivenObject  the given object.
     * @param  theClass        the class.
     * @param  <OBJECT>        the data type of the given object.
     * @param  <CLASS>         the data type of the returned object.
     * @return  the original object as the type class or null.
     */
    public <OBJECT, CLASS> CLASS as(OBJECT theGivenObject, Class<CLASS> theClass) {
        if (theClass.isInstance(theGivenObject))
            return theClass.cast(theGivenObject);
        return null;
    }
    
    /**
     * Map the given object using the transformation if the given object is not null or else return null.
     * 
     * This method is the alias of mapBy and mapFrom.
     * 
     * @param  theGivenObject  the given object.
     * @param  transformation  the transformation function.
     * @param  <OBJECT>        the data type of the given object.
     * @param  <TARGET>        the data type of the target object.
     * @return  the transformed value.
     */
    public <OBJECT, TARGET> TARGET mapTo(OBJECT theGivenObject, Function<OBJECT, TARGET> transformation) {
        return (theGivenObject != null) ? transformation.apply(theGivenObject) : null;
    }
    
    /**
     * Map the given object using the transformation if the given object is not null or else return null.
     * 
     * This method is the alias of mapTo and mapFrom.
     * 
     * @param  theGivenObject  the given object.
     * @param  transformation  the transformation function.
     * @param  <OBJECT>        the data type of the given object.
     * @param  <TARGET>        the data type of the target object.
     * @return  the transformed value.
     */
    public <OBJECT, TARGET> TARGET mapBy(OBJECT theGivenObject, Function<OBJECT, TARGET> transformation) {
        return (theGivenObject != null) ? transformation.apply(theGivenObject) : null;
    }
    
    /**
     * Map the given object using the transformation if the given object is not null or else return null.
     * 
     * This method is the alias of mapTo and mapBy.
     * 
     * @param  theGivenObject  the given object.
     * @param  transformation  the transformation function.
     * @param  <OBJECT>        the data type of the given object.
     * @param  <TARGET>        the data type of the target object.
     * @return the transformed value.
     */
    public <OBJECT, TARGET> TARGET mapFrom(OBJECT theGivenObject, Function<OBJECT, TARGET> transformation) {
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
     * Returns elseValue if theGivenLong is null. 
     * 
     * @param  theGivenLong  the given long.
     * @param  elseValue     the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public long or(Long theGivenLong, int elseValue) {
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
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenDouble  the given double.
     * @param  elseValue       the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public double or(Double theGivenDouble, int elseValue) {
        return (theGivenDouble == null) ? elseValue : theGivenDouble.doubleValue();
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigInteger  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public BigInteger or(BigInteger theGivenBigInteger, int elseValue) {
        return (theGivenBigInteger == null) ? BigInteger.valueOf(elseValue) : theGivenBigInteger;
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigInteger  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public BigInteger or(BigInteger theGivenBigInteger, long elseValue) {
        return (theGivenBigInteger == null) ? BigInteger.valueOf(elseValue) : theGivenBigInteger;
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigDecimal  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public BigDecimal or(BigDecimal theGivenBigDecimal, int elseValue) {
        return (theGivenBigDecimal == null) ? BigDecimal.valueOf(elseValue) : theGivenBigDecimal;
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigDecimal  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public BigDecimal or(BigDecimal theGivenBigDecimal, long elseValue) {
        return (theGivenBigDecimal == null) ? BigDecimal.valueOf(elseValue) : theGivenBigDecimal;
    }
    
    /**
     * Returns elseValue if theGivenDouble is null. 
     * 
     * @param  theGivenBigDecimal  the given BigInteger.
     * @param  elseValue           the return value for when the given object is null.
     * @return theGivenObject if not null or elseValue if null.
     **/
    public BigDecimal or(BigDecimal theGivenBigDecimal, double elseValue) {
        return (theGivenBigDecimal == null) ? BigDecimal.valueOf(elseValue) : theGivenBigDecimal;
    }
    
}
