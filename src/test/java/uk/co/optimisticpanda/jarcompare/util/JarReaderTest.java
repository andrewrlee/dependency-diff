package uk.co.optimisticpanda.jarcompare.util;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.optimisticpanda.jarcompare.test.util.TestUtils.getFile;

import java.io.File;
import java.util.function.ToLongFunction;

import org.junit.Test;

import rx.Observable;
import uk.co.optimisticpanda.jarcompare.ClassFile;
import uk.co.optimisticpanda.jarcompare.JarContents;

public class JarReaderTest {

	private static final File VERSION_0_0_1_FILE = getFile("test-0.0.1-SNAPSHOT.jar");
	private static final ToLongFunction<ClassFile> countOfDescendentsAndThis = cf -> 1L + cf
			.getNestedClassesAndDescendentsSize();

	@Test
	public void checkNumberOfClassesIsSameAsNumberOfCollectedClassesWithNestedClasses() {

		Long totalClassCount = JarReader.packages(VERSION_0_0_1_FILE)
				.flatMap(Observable::from).countLong().toBlocking().single();

		Long collectedClassCount = JarContents.load(VERSION_0_0_1_FILE)
				.stream().mapToLong(countOfDescendentsAndThis).sum();

		assertThat(totalClassCount).isEqualTo(collectedClassCount);
	}
}
