package me.goodt.vkpht.common.dictionary.core.service;

/**
 * Represents a converter that convert one object to another.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #convert(Object)}}.
 *
 * @param <T> the type of the input object for the converter
 * @param <R> the type of the resulting object of the converter
 */
@FunctionalInterface
public interface Converter<T, R> {

    /**
     * Converts the given argument to some object.
     *
     * @param object the converter argument
     * @return the some object
     */
    R convert(T object);
}
