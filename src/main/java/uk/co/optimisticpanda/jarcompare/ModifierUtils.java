package uk.co.optimisticpanda.jarcompare;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.IntPredicate;

import javassist.Modifier;

public class ModifierUtils {

	public enum Mod {
		PUBLIC, PRIVATE, PROTECTED, PACKAGE, STATIC, FINAL, SYNCHRONIZED, VOLATILE, VARARGS, TRANSIENT, NATIVE, INTERFACE, ABSTRACT, STRICT, ANNOTATION, ENUM;
	}

	public static SortedSet<Mod> getModifiers(int modifier) {
		TreeSet<Mod> results = new TreeSet<>();
		BiConsumer<IntPredicate, Mod> adder = createAdder(results, modifier);

		adder.accept(Modifier::isPublic, Mod.PUBLIC);
		adder.accept(Modifier::isPrivate, Mod.PRIVATE);
		adder.accept(Modifier::isProtected, Mod.PROTECTED);
		adder.accept(Modifier::isPackage, Mod.PACKAGE);
		adder.accept(Modifier::isStatic, Mod.STATIC);
		adder.accept(Modifier::isFinal, Mod.FINAL);
		adder.accept(Modifier::isSynchronized, Mod.SYNCHRONIZED);
		adder.accept(Modifier::isVolatile, Mod.VOLATILE);
		adder.accept(Modifier::isTransient, Mod.TRANSIENT);
		adder.accept(Modifier::isNative, Mod.NATIVE);
		adder.accept(Modifier::isInterface, Mod.INTERFACE);
		adder.accept(Modifier::isAnnotation, Mod.ANNOTATION);
		adder.accept(Modifier::isEnum, Mod.ENUM);
		adder.accept(Modifier::isAbstract, Mod.ABSTRACT);
		adder.accept(Modifier::isStrict, Mod.STRICT);
		return results;
	}

	private static BiConsumer<IntPredicate, Mod> createAdder(Collection<Mod> result, int mod) {
		return (IntPredicate predicate, Mod modifier) -> {
			if (predicate.test(mod)) {
				result.add(modifier);
			}
		};
	}
}
