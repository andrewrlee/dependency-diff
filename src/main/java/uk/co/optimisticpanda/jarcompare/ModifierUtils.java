package uk.co.optimisticpanda.jarcompare;

import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.ABSTRACT;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.ANNOTATION;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.ENUM;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.FINAL;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.INTERFACE;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.NATIVE;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.PACKAGE;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.PRIVATE;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.PROTECTED;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.PUBLIC;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.STATIC;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.STRICT;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.SYNCHRONIZED;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.TRANSIENT;
import static uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod.VOLATILE;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.IntPredicate;

import javassist.Modifier;

public class ModifierUtils {

	public enum Mod {
		PUBLIC, PRIVATE, PROTECTED, PACKAGE, STATIC, FINAL, SYNCHRONIZED, VOLATILE, VARARGS, TRANSIENT, NATIVE, INTERFACE, ABSTRACT, STRICT, ANNOTATION, ENUM;
	}

	public static SortedSet<Mod> getModifiers(int modifier) {
		TreeSet<Mod> results = new TreeSet<>();
		Gatherer gatherer = createGatherer(results, modifier);

		gatherer.addIf(Modifier::isPublic, PUBLIC);
		gatherer.addIf(Modifier::isPrivate, PRIVATE);
		gatherer.addIf(Modifier::isProtected, PROTECTED);
		gatherer.addIf(Modifier::isPackage, PACKAGE);
		gatherer.addIf(Modifier::isStatic, STATIC);
		gatherer.addIf(Modifier::isFinal, FINAL);
		gatherer.addIf(Modifier::isSynchronized, SYNCHRONIZED);
		gatherer.addIf(Modifier::isVolatile, VOLATILE);
		gatherer.addIf(Modifier::isTransient, TRANSIENT);
		gatherer.addIf(Modifier::isNative, NATIVE);
		gatherer.addIf(Modifier::isInterface, INTERFACE);
		gatherer.addIf(Modifier::isAnnotation, ANNOTATION);
		gatherer.addIf(Modifier::isEnum, ENUM);
		gatherer.addIf(Modifier::isAbstract, ABSTRACT);
		gatherer.addIf(Modifier::isStrict, STRICT);
		return results;
	}

	private static Gatherer createGatherer(Collection<Mod> result, int mod) {
		return (predicate, modifier) -> {
			if (predicate.test(mod)) {
				result.add(modifier);
			}
		};
	}
	private interface Gatherer {
		void addIf(IntPredicate predicate, Mod modifier);
	}
}
