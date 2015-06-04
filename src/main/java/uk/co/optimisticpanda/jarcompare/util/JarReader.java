package uk.co.optimisticpanda.jarcompare.util;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.ClassPool;
import javassist.CtClass;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import uk.co.optimisticpanda.jarcompare.ClassFile;

import com.google.common.base.Throwables;

public enum JarReader {
	;

	private static final String PACKAGE_AND_BASE_CLASS_NAME = "(.*?)(\\$|\\.class)";

	private final static Func1<JarFileAndEntry, Boolean> CLASSES_ONLY_RX = entry -> entry
			.getName().endsWith(".class");

	/** Creates an observable containing the package contents. */
	public static Observable<List<JarFileAndEntry>> packages(File file) {
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

		Func1<JarFile, Observable<JarFileAndEntry>> observableFactory = jarFile -> {
			return Observable.create((subscriber) -> {
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()
						&& !subscriber.isUnsubscribed()) {
					subscriber.onNext(new JarFileAndEntry(jarFile, entries
							.nextElement()));
				}
				if (!subscriber.isUnsubscribed()) {
					subscriber.onCompleted();
				}
			});
		};

		return Observable
				.using(resourceFactory, observableFactory, disposeAction)
				.filter(CLASSES_ONLY_RX).groupBy(baseClass())
				.flatMap(group -> group.toList());
	}

	/** Creates an observable of class files */
	public static Observable<ClassFile> classes(File file) {
		return packages(file).map(toClasses()).flatMap(Observable::from);
	}

	/**
	 * Resolves the top most class. So ClassA and ClassA$ClassA would both
	 * resolve to ClassA
	 */
	private static Func1<JarFileAndEntry, String> baseClass() {
		Pattern pattern = Pattern.compile(PACKAGE_AND_BASE_CLASS_NAME);
		return (entry) -> {
			Matcher matcher = pattern.matcher(entry.getName());
			matcher.find();
			return matcher.group(1);
		};
	}

	/**
	 * Loads a package of classes into the class pool and then creates the
	 * associated ClassFiles.
	 */
	private static Func1<List<JarFileAndEntry>, List<ClassFile>> toClasses() {
		return entries -> {
			ClassPool pool = new ClassPool(null);
			pool.appendSystemPath();

			List<CtClass> classes = entries.stream().map(loadClass(pool))
					.collect(toList());

			return classes.stream()
					.filter(entry -> !entry.getName().contains("$"))
					.map(ClassFile::new).collect(toList());
		};
	}

	private static Function<JarFileAndEntry, CtClass> loadClass(ClassPool pool) {
		return entry -> {
			try (InputStream stream = entry.getInputStream()) {
				return pool.makeClass(stream);
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		};
	}

	public static class JarFileAndEntry {

		private JarFile jarFile;
		private JarEntry jarEntry;

		private JarFileAndEntry(JarFile jarFile, JarEntry jarEntry) {
			this.jarFile = jarFile;
			this.jarEntry = jarEntry;
		}

		public InputStream getInputStream() throws IOException {
			return jarFile.getInputStream(jarEntry);
		}

		public String getName() {
			return jarEntry.getName();
		}
	}
}
