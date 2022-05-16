package com.bobocode;

import com.bobocode.model.Account;
import lombok.SneakyThrows;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomFieldComparator<T extends  Account> implements Comparator<T> {


    List<Field> collect;
    Field activeField;

    String classType;
    Method[] methods;

    public RandomFieldComparator(Class<T> targetType) {
        this(targetType, true);
    }

    /**
     * A constructor that accepts a class and a property indicating which fields can be used for comparison. If property
     * value is true, then only public fields or fields with public getters can be used.
     *
     * @param targetType                  a type of objects that may be compared
     * @param compareOnlyAccessibleFields config property indicating if only publicly accessible fields can be used
     */
    public RandomFieldComparator(Class<T> targetType, boolean compareOnlyAccessibleFields) {

        this.methods = targetType.getDeclaredMethods();
        this.classType = targetType.getSimpleName();

         if (compareOnlyAccessibleFields) {
             this.collect = Arrays.stream(targetType.getDeclaredFields())
                     .filter(x -> x.getModifiers() != Modifier.PRIVATE
                             || checkGetMethod(x)
                             || Comparable.class.isAssignableFrom(x.getType()))
                     .peek(x -> x.setAccessible(true))
                     .collect(Collectors.toList());
         } else {
             this.collect = Arrays.stream(targetType.getDeclaredFields())
                     .filter(x -> Comparable.class.isAssignableFrom(x.getType()))
                     .peek(x -> x.setAccessible(true))
                     .collect(Collectors.toList());
         }

        int abs = (int) Math.abs(new Date().getTime() % collect.size());
        this.activeField = collect.get(abs);
    }

    private boolean checkGetMethod(Field x) {
        String name = "get" + x.getName().substring(0,1).toUpperCase() + x.getName().substring(1);
        for (int i = 0; i < methods.length; i++) {
            if(methods[i].getName().equals(name) && methods[i].getModifiers() == Modifier.PUBLIC){
                return true;
            }
        }
        return false;
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value grater than a non-null value (nulls last).
     */
    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public int compare(T o1, T o2) {
        var firstParam = (Comparable) activeField.get(o1);
        var secondParam = (Comparable) activeField.get(o2);
        if(activeField.get(o1) != null && activeField.get(o2) == null){
            return -1;
        }
        if(activeField.get(o1) == null && activeField.get(o2) != null){
            return 1;
        }
        if(activeField.get(o1) == null && activeField.get(o2) == null){
            return 0;
        }
        return firstParam.compareTo(secondParam);
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return String.format("Class name %s and field %s", classType,  activeField.getName());
    }
}
