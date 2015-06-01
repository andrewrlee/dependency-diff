package uk.co.optimisticpanda.jarcompare.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javassist.ClassPool;
import javassist.CtClass;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import com.google.common.base.Throwables;

public class JarReader {

	private static final String PACKAGE_AND_BASE_CLASS_NAME = "(.*?)(\\$|\\.class)";
	
	private final static Predicate<JarEntry> CLASSES_ONLY = entry -> entry
			.getName().endsWith(".class");
	
	private final static Func1<JarEntry, Boolean> CLASSES_ONLY_RX = entry -> entry
			.getName().endsWith(".class"); 
	
	public static Func1<JarEntry, String> baseClass() {
		Pattern pattern = Pattern.compile(PACKAGE_AND_BASE_CLASS_NAME);
		return (entry) -> {
			Matcher matcher = pattern.matcher(entry.getName());
			matcher.find();
			return matcher.group(1);
		};
	}

	
	public static <T> StreamReturn<T> entriesOf(File file) {
		ClassPool pool = new ClassPool(null);
		pool.appendSystemPath();
		return (func) -> {
			try (JarFile jarFile = new JarFile(file)) {

				return func.apply(jarFile.stream().filter(CLASSES_ONLY)
						.map(jarEntryToClass(pool, jarFile)));
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		};
	}

	public static Observable<List<JarEntry>> packages(File file) {
		Func0<JarFile> resourceFactory = () -> {
			try {
				return new JarFile(file);
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		};

		Action1<JarFile> disposeAction = (jarFile -> {
			try {
				jarFile.close();
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		});

		Func1<JarFile, Observable<JarEntry>> observableFactory = (jarFile) -> {
			return Observable.create((subscriber) -> {
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()
						&& !subscriber.isUnsubscribed()) {
					subscriber.onNext(entries.nextElement());
				}
				if (!subscriber.isUnsubscribed()) {
					subscriber.onCompleted();
				}
			});
		};

		return Observable.using(resourceFactory, observableFactory, disposeAction)
				.filter(CLASSES_ONLY_RX)
				.groupBy(baseClass())
				.flatMap(group -> group.toList());
	}

	
	
	public interface StreamReturn<T> {
		T map(Function<Stream<CtClass>, T> function);

		default void stream(Consumer<Stream<CtClass>> consumer) {
			this.map(FunctionUtils.sinkFunction(consumer));
		}
	}

	private static Function<JarEntry, CtClass> jarEntryToClass(ClassPool pool,
			JarFile file) {
		return entry -> {
			try (InputStream stream = file.getInputStream(entry)) {
				return pool.makeClass(stream);
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		};
	}
}
