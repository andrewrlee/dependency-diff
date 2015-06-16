package uk.co.optimisticpanda.jarcompare.util;

import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.ABSTRACT;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.ANNOTATION;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.ENUM;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.FINAL;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.INTERFACE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.NATIVE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PACKAGE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PRIVATE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PROTECTED;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PUBLIC;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.STATIC;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.STRICT;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.SYNCHRONIZED;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.TRANSIENT;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.VOLATILE;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.IntPredicate;

import javassist.CtClass;
import javassist.Modifier;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.ClassFile;

public class ModifierUtils {

	public enum Mod {
		PUBLIC, PRIVATE, PROTECTED, PACKAGE, STATIC, FINAL, SYNCHRONIZED, VOLATILE, VARARGS, TRANSIENT, NATIVE, INTERFACE, ABSTRACT, STRICT, ANNOTATION, ENUM;
	}

	public static SortedSet<Mod> getModifiers(CtClass ctClass) {
		TreeSet<Mod> results = new TreeSet<>();

		ClassFile classFile = ctClass.getClassFile();
		
		int accessFlag = classFile.getInnerAccessFlags() == -1 ? classFile.getAccessFlags() : classFile.getInnerAccessFlags();
	
		Gatherer accessGatherer = createGatherer(results, accessFlag);
		accessGatherer.addIf(AccessFlag::isPublic, PUBLIC);
		accessGatherer.addIf(AccessFlag::isPrivate, PRIVATE);
		accessGatherer.addIf(AccessFlag::isProtected, PROTECTED);
		accessGatherer.addIf(AccessFlag::isPackage, PACKAGE);
		
		Gatherer gatherer = createGatherer(results, ctClass.getModifiers());
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
