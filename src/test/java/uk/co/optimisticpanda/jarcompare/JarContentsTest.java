package uk.co.optimisticpanda.jarcompare;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.function.ToLongFunction;

import org.junit.Test;

import uk.co.optimisticpanda.jarcompare.diff.Differences;
import uk.co.optimisticpanda.jarcompare.util.JarReader;

public class JarContentsTest {

	private static final File JUNIT_4_13_FILE = new File("junit-4.11.jar");
	private static final File JUNIT_4_12_FILE = new File("junit-4.12.jar");
	private static final ToLongFunction<ClassFile> countOfDescendentsAndThis = 
			cf -> 1L + cf.getNestedClassesAndDescendentsSize();
	
	
	@Test
	public void checkNumberOfClassesIsSameAsNumberOfCollectedClassesWithNestedClasses(){
		
		Long totalClassCount = JarReader.<Long>entriesOf(JUNIT_4_12_FILE).map(s -> s.count());
		
		Long collectedClassCount = JarContents.load(JUNIT_4_12_FILE).stream()
				.mapToLong(countOfDescendentsAndThis).sum();
		
		assertThat(totalClassCount).isEqualTo(collectedClassCount);
	}

	@Test
	public void check() throws IOException {

		JarContents junit_4_11 = JarContents.load(JUNIT_4_13_FILE);
		JarContents junit_4_12 = JarContents.load(JUNIT_4_12_FILE);

		Differences differences = junit_4_11.difference(junit_4_12);
		System.out.println(differences);
	}
}
