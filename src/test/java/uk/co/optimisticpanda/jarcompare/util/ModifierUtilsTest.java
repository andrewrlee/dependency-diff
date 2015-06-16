package uk.co.optimisticpanda.jarcompare.util;

import static uk.co.optimisticpanda.jarcompare.test.util.ModifierAssertions.classPool;
import static uk.co.optimisticpanda.jarcompare.test.util.ModifierAssertions.in;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.ABSTRACT;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.ANNOTATION;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.INTERFACE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PACKAGE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PRIVATE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PROTECTED;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PUBLIC;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.STATIC;

import java.io.IOException;

import javassist.NotFoundException;

import org.junit.Test;

import uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass;
import uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass.AbstractTest;
import uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass.AnnotationTest;
import uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass.InterfaceTest;
import uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass.PublicTest;
import uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass.StaticTest;
import uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass.StrictfpTest;

public class ModifierUtilsTest {

	@Test
	public void checkModifier() throws IOException, NotFoundException{
		in(classPool())
			.check(ModifierTestClass.class).contains(PUBLIC)
			.check(AbstractTest.class).contains(PUBLIC, ABSTRACT)
			.check(PublicTest.class).contains(PUBLIC)
			.check("uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass$ProtectedTest").contains(PROTECTED)
			.check("uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass$PrivateTest").contains(PRIVATE)
			.check("uk.co.optimisticpanda.jarcompare.util.data.ModifierTestClass$PackageTest").contains(PACKAGE)
			.check(StaticTest.class).contains(PUBLIC, STATIC)
			.check(InterfaceTest.class).contains(PUBLIC, STATIC, INTERFACE, ABSTRACT )
			.check(AnnotationTest.class).contains(PUBLIC, STATIC, INTERFACE, ABSTRACT, ANNOTATION)
			
			// This doesn't detect strictfp
			.check(StrictfpTest.class).contains(PUBLIC);
	}
}
