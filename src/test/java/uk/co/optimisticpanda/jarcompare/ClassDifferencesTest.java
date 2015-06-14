package uk.co.optimisticpanda.jarcompare;

import static uk.co.optimisticpanda.jarcompare.test.util.TestAssertions.check;
import static uk.co.optimisticpanda.jarcompare.test.util.TestUtils.differenceBetween;

import java.io.IOException;

import org.junit.Test;

import uk.co.optimisticpanda.jarcompare.diff.Differences;


public class ClassDifferencesTest {

	Differences differences = differenceBetween("test-0.0.1-SNAPSHOT.jar").and("test-0.0.2-SNAPSHOT.jar");

	@Test
	public void checkClassAdditions() throws IOException {
		check(differences)
				.additionalClassesAre(
						"com.test.class1.NewClass",
						"com.test.class2.MovedClass",
						"com.test.class1.ClassWithNewNestedClass$NewNestedClass",
						"com.test.class1.ClassWithAdditionalNestedClassesAtSameLevel$NewNestedClassA",
						"com.test.class1.ClassWithAdditionalNestedClassesAtSameLevel$NewNestedClassB",
						"com.test.class1.ClassWithNewNestedNestedClass$NewNestedClass$NewNestedNestedClass",
						"com.test.class1.ClassWithTwoLevelsOfNestingAdded$NewNestedClass",
						"com.test.class1.ClassWithTwoLevelsOfNestingAdded$NewNestedClass$NewNestedNestedClass");
	}

	@Test
	public void checkClassRemovals() throws IOException {
		check(differences)
				.removedClassesAre(
						"com.test.class1.MovedClass",
						"com.test.class1.RemovedClass",
						"com.test.class1.RemovedNestedClass$NewNestedClass",
						"com.test.class1.RemovedNestedNestedClass$NewNestedClass$NewNestedNestedClass",
						"com.test.class1.ClassWithTwoLevelsOfNestingRemoved$NewNestedClass",
						"com.test.class1.ClassWithTwoLevelsOfNestingRemoved$NewNestedClass$NewNestedNestedClass");
	}
}
