package uk.co.optimisticpanda.jarcompare.util;

import static java.util.Spliterator.IMMUTABLE;

import java.util.Enumeration;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Iterators;


public class FunctionUtils {

	public static <T> Stream<T> enumerationStream(Enumeration<T> enumeration){
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(Iterators.forEnumeration(enumeration), IMMUTABLE), false);
		
	}
	
	public static <T,R> Function<T, R> sinkFunction(Consumer<T> consumer){
		return new Function<T, R>(){
			@Override
			public R apply(T t) {
				consumer.accept(t);
				return null;
			}
		};
	}
	
}
