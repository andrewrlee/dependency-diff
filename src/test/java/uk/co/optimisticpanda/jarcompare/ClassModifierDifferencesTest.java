package uk.co.optimisticpanda.jarcompare;

import static uk.co.optimisticpanda.jarcompare.test.util.TestAssertions.assertThat;
import static uk.co.optimisticpanda.jarcompare.test.util.TestUtils.differenceBetween;

import java.io.IOException;

import org.junit.Test;

import uk.co.optimisticpanda.jarcompare.diff.Differences;


public class ClassModifierDifferencesTest {

	Differences differences = differenceBetween("test-0.0.1-SNAPSHOT.jar").and("test-0.0.2-SNAPSHOT.jar");

	@Test
	public void checkModifierDifferences() throws IOException {
		System.out.println(differences);
	}
}
