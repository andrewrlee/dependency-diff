package uk.co.optimisticpanda.jarcompare;

import static uk.co.optimisticpanda.jarcompare.test.util.TestAssertions.assertThat;
import static uk.co.optimisticpanda.jarcompare.test.util.TestUtils.getFile;

import java.io.File;
import java.io.IOException;
import java.util.function.ToLongFunction;

import org.junit.Test;

import uk.co.optimisticpanda.jarcompare.diff.Differences;
import uk.co.optimisticpanda.jarcompare.util.JarReader;

public class JarContentsTest {

	private static final File VERSION_0_0_1_FILE = getFile("test-0.0.1-SNAPSHOT.jar");
	private static final File VERSION_0_0_2_FILE = getFile("test-0.0.2-SNAPSHOT.jar");
	private static final ToLongFunction<ClassFile> countOfDescendentsAndThis = cf -> 1L + cf
			.getNestedClassesAndDescendentsSize();

	@Test
	public void checkNumberOfClassesIsSameAsNumberOfCollectedClassesWithNestedClasses() {

		Long totalClassCount = JarReader.<Long> entriesOf(VERSION_0_0_1_FILE)
				.map(s -> s.count());

		Long collectedClassCount = JarContents.load(VERSION_0_0_1_FILE)
				.stream().mapToLong(countOfDescendentsAndThis).sum();

		assertThat(totalClassCount).isEqualTo(collectedClassCount);
	}

	@Test
	public void checkClassAdditions() throws IOException {

		JarContents version1 = JarContents.load(VERSION_0_0_1_FILE);
		JarContents version2 = JarContents.load(VERSION_0_0_2_FILE);

		Differences differences = version1.difference(version2);

		assertThat(differences).additionalClassesAre(
				"com.test.package1.ClassF", "com.test.package2.ClassE",
				"com.test.package3.ClassG", "com.test.package3.ClassH");

		assertThat(differences).removedClassesAre("com.test.package1.ClassG",
				"com.test.package2.ClassD");
	}

	@Test
	public void checkModifierDifferences() throws IOException {

		// JarContents version1 = JarContents.load(VERSION_0_0_1_FILE);
		JarContents version2 = JarContents.load(VERSION_0_0_2_FILE);

		// Differences differences = version1.difference(version2);

		// System.out.println(differences);
	}
}
