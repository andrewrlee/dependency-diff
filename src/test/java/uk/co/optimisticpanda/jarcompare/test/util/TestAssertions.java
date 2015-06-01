package uk.co.optimisticpanda.jarcompare.test.util;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import uk.co.optimisticpanda.jarcompare.diff.ClassDifferences;
import uk.co.optimisticpanda.jarcompare.diff.Differences;

public class TestAssertions extends Assertions {

	public static DifferencesAssert assertThat(Differences actual) {
		return new DifferencesAssert(actual);
	}

	public static class DifferencesAssert extends
			AbstractAssert<DifferencesAssert, Differences> {

		public DifferencesAssert(Differences actual) {
			super(actual, DifferencesAssert.class);
		}

		public DifferencesAssert additionalClassesAre(String... names) {
			isNotNull();
			ClassDifferences classDifference = actual.getClassDifference();
			assertThat(classDifference.getAdditionSize())
					.as("Expected number of class additions was incorrect")
					.isEqualTo(names.length);
			
			for (String name: names) {
				if (!classDifference.hasAddition(name)) {
					failWithMessage(
							"Expected differences: <%s> to include addition: <%s>",
							classDifference, name);
				}	
			}
			return this;
		}
		public DifferencesAssert removedClassesAre(String... names) {
			isNotNull();
			ClassDifferences classDifference = actual.getClassDifference();
			assertThat(classDifference.getRemovalSize())
					.as("Expected number of class removals was incorrect") 
					.isEqualTo(names.length);
			for (String name: names) {
				if (!classDifference.hasRemoval(name)) {
					failWithMessage(
							"Expected differences: <%s> to include removal: <%s>",
							classDifference, name);
				}	
			}
			return this;
		}
	}
}
