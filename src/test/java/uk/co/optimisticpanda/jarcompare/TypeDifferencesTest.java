package uk.co.optimisticpanda.jarcompare;

import static uk.co.optimisticpanda.jarcompare.diff.Differences.differenceBetween;
import static uk.co.optimisticpanda.jarcompare.test.util.TestAssertions.check;
import static uk.co.optimisticpanda.jarcompare.test.util.TestUtils.getFile;
import static uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod.*;

import java.io.IOException;

import org.junit.Test;

import uk.co.optimisticpanda.jarcompare.diff.Differences;
import uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod;

public class TypeDifferencesTest {

	private static final Mod[] CLASS_MODS = new Mod[]{PUBLIC};
	private static final Mod[] INTERFACE_MODS = new Mod[]{PUBLIC, INTERFACE, ABSTRACT};
	private static final Mod[] ANNOTATION_MODS = new Mod[]{PUBLIC, INTERFACE, ABSTRACT, ANNOTATION};
	
	Differences differences = differenceBetween(getFile("test-0.0.1-SNAPSHOT.jar")).and(getFile("test-0.0.2-SNAPSHOT.jar"));

	@Test
	public void checkTypeChanges() throws IOException {
		
		check(differences)
			
			.classModifiersFor("com.test.types.ClassToInterface")
				.were(CLASS_MODS).now(INTERFACE_MODS).end()
			.classModifiersFor("com.test.types.ClassToAnnotation")
				.were(CLASS_MODS).now(ANNOTATION_MODS).end()
			
			.classModifiersFor("com.test.types.InterfaceToClass")
				.were(INTERFACE_MODS).now(PUBLIC).end()
			.classModifiersFor("com.test.types.InterfaceToAnnotation")
				.were(INTERFACE_MODS).now(ANNOTATION_MODS).end()
			
			.classModifiersFor("com.test.types.AnnotationToInterface")
				.were(ANNOTATION_MODS).now(INTERFACE_MODS).end()
			.classModifiersFor("com.test.types.AnnotationToClass")
				.were(ANNOTATION_MODS).now(CLASS_MODS).end();
	}
}
