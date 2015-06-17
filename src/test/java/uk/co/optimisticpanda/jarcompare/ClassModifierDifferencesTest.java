package uk.co.optimisticpanda.jarcompare;

import static uk.co.optimisticpanda.jarcompare.diff.Differences.differenceBetween;
import static uk.co.optimisticpanda.jarcompare.test.util.TestAssertions.check;
import static uk.co.optimisticpanda.jarcompare.test.util.TestUtils.getFile;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.ABSTRACT;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.FINAL;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PACKAGE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PRIVATE;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.PUBLIC;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.STATIC;

import java.io.IOException;

import org.junit.Test;

import uk.co.optimisticpanda.jarcompare.diff.Differences;

public class ClassModifierDifferencesTest {

	Differences differences = differenceBetween(getFile("test-0.0.1-SNAPSHOT.jar")).and(getFile("test-0.0.2-SNAPSHOT.jar"));

	@Test
	public void checkClassModifierChanges() throws IOException {

		check(differences)
			
			.classModifiersFor("com.test.classModifiers1.ModifierChangeClass")
				.were(PUBLIC, FINAL).now(PUBLIC).end()
			
			.classModifiersFor("com.test.classModifiers1.ModifierAndSubclassChangeClass")
				.were(PUBLIC).now(PUBLIC, FINAL)
				.subClassModifiersFor("com.test.classModifiers1.ModifierAndSubclassChangeClass$ModifierAndSubclassChangeSubClass")
					.were(PUBLIC).now(PACKAGE, STATIC, FINAL).end()
				.end()
				
			.classModifiersFor("com.test.classModifiers1.NestedNestedClassModifierChangesClass")
				.were(PUBLIC).now(PUBLIC)
				.subClassModifiersFor("com.test.classModifiers1.NestedNestedClassModifierChangesClass$NestedClass")
					.were(PUBLIC).now(PUBLIC)
					.subClassModifiersFor("com.test.classModifiers1.NestedNestedClassModifierChangesClass$NestedClass$NestedNestedClass")
						.were(PUBLIC).now(PRIVATE, ABSTRACT).end()
					.end()
				.end()
		
			.classModifiersFor("com.test.classModifiers1.EnclosingNestedNestedClassModifierChangesClass")
				.were(PUBLIC).now(PACKAGE, ABSTRACT)
				.subClassModifiersFor("com.test.classModifiers1.EnclosingNestedNestedClassModifierChangesClass$NestedClass")
					.were(PUBLIC).now(PACKAGE, ABSTRACT)
					.subClassModifiersFor("com.test.classModifiers1.EnclosingNestedNestedClassModifierChangesClass$NestedClass$NestedNestedClass")
						.were(PUBLIC).now(PACKAGE, ABSTRACT).end()
					.end()
				.end()
				
			.classModifiersFor("com.test.classModifiers1.NestedNestedModifierChangesClass")
				.were(PUBLIC).now(PUBLIC)
				.subClassModifiersFor("com.test.classModifiers1.NestedNestedModifierChangesClass$NestedClass")
					.were(PUBLIC).now(PRIVATE, ABSTRACT).end()
			.end();
	}
}
