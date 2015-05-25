package uk.co.optimisticpanda.jarcompare.util;

import static uk.co.optimisticpanda.jarcompare.util.FunctionUtils.enumerationStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import javassist.ClassPool;
import javassist.CtClass;

import com.google.common.base.Throwables;

public class JarReader {

	private final static Predicate<JarEntry> CLASSES_ONLY = entry -> entry.getName().endsWith(".class");

	public static <T> StreamReturn<T> entriesOf(File file) {
		ClassPool pool = ClassPool.getDefault();
		return (func) -> {
			try (JarFile jarFile = new JarFile(file)) {
				
				return func.apply(
						enumerationStream(jarFile.entries())
							.filter(CLASSES_ONLY)
							.map(jarEntryToClass(pool, jarFile)));
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		};
	}

	public interface StreamReturn<T> {
		T map(Function<Stream<CtClass>, T> function);
		
		default void stream(Consumer<Stream<CtClass>> consumer){
			this.map(FunctionUtils.sinkFunction(consumer));
		}
	}

	private static Function<JarEntry, CtClass> jarEntryToClass(ClassPool pool, JarFile file) {
		return entry -> {
			try (InputStream stream = file.getInputStream(entry)) {
				return pool.makeClass(stream);
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		};
	}
}
