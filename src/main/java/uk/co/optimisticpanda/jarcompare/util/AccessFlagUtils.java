package uk.co.optimisticpanda.jarcompare.util;

import static uk.co.optimisticpanda.jarcompare.util.AccessFlagUtils.Flag.*;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.IntPredicate;

import javassist.Modifier;
import javassist.bytecode.AccessFlag;

public class AccessFlagUtils {

	public enum Flag {
		PUBLIC,
		PRIVATE,
		PROTECTED,    
		STATIC,       
		FINAL,        
		SYNCHRONIZED,
		VOLATILE,
		TRANSIENT,
		NATIVE,       
		INTERFACE,
		ABSTRACT,
		STRICT,
		PACKAGE,
		SUPER;
	}
	
	
	
	public static SortedSet<Flag> getFlags(int accessFlag){
		TreeSet<Flag> results = new TreeSet<>();
		Gatherer gatherer = createGatherer(results, accessFlag);

		gatherer.addIf(AccessFlag::isPublic, PUBLIC);
		gatherer.addIf(AccessFlag::isPrivate, PRIVATE);
		gatherer.addIf(AccessFlag::isProtected, PROTECTED);
		gatherer.addIf(AccessFlag::isPackage, PACKAGE);
		return results;
	}

	private static Gatherer createGatherer(Collection<Flag> result, int mod) {
		return (predicate, modifier) -> {
			if (predicate.test(mod)) {
				result.add(modifier);
			}
		};
	}
	private interface Gatherer {
		void addIf(IntPredicate predicate, Flag flag);
	}
	
}
