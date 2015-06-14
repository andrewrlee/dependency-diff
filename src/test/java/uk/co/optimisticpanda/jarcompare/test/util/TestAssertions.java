package uk.co.optimisticpanda.jarcompare.test.util;

import java.util.Arrays;
import java.util.Optional;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import uk.co.optimisticpanda.jarcompare.diff.ClassDifferences;
import uk.co.optimisticpanda.jarcompare.diff.ClassModifierDifferences.ModifierDiff;
import uk.co.optimisticpanda.jarcompare.diff.Differences;
import uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod;

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

		public TopLevelClassModifierVerifier classModifiersFor(String className) {
			Optional<ModifierDiff> dif = actual.getClassModifierDifference().getDifference(className);
			assertThat(dif).isNotNull();
			return new TopLevelClassModifierVerifier(this, dif.get());
		}
		
		public abstract static class ClassModifierVerifier<THIS extends ClassModifierVerifier<THIS>>{

			protected ModifierDiff difference;

			public ClassModifierVerifier(ModifierDiff diff) {
				this.difference = diff;
			}

			@SuppressWarnings("unchecked")
			public THIS were(Mod... mods) {
				assertThat(difference.previouslyHad(mods))
				.as("Expected %s to previously contain %s", difference, Arrays.toString(mods)).isTrue();
				return (THIS) this;
			}
			
			@SuppressWarnings("unchecked")
			public THIS now(Mod... mods) {
				assertThat(difference.nowHas(mods))
				.as("Expected %s to now contain %s", difference, Arrays.toString(mods)).isTrue();
				return (THIS) this;
			}

		}  
		
		public static class TopLevelClassModifierVerifier extends ClassModifierVerifier<TopLevelClassModifierVerifier> {

			private DifferencesAssert differencesAssert;

			public TopLevelClassModifierVerifier(
					DifferencesAssert differencesAssert, ModifierDiff diff) {
				super(diff);
				this.differencesAssert = differencesAssert;
			}
			
			public SubClassModifierVerifier<TopLevelClassModifierVerifier> subClassModifiersFor(String subclassName) {
				Optional<ModifierDiff> dif = difference.getSubClassDifference(subclassName);
				assertThat(dif).isNotNull();
				
				return new SubClassModifierVerifier<TopLevelClassModifierVerifier>(this, dif.get());
			}
			
			public DifferencesAssert end() {
				return differencesAssert;
			}
		}
		
		public static class SubClassModifierVerifier<PARENT extends ClassModifierVerifier<PARENT>> extends ClassModifierVerifier<SubClassModifierVerifier<PARENT>> {

			private PARENT parent;

			public SubClassModifierVerifier(
					PARENT parent, ModifierDiff diff) {
				super(diff);
				this.parent = parent;
			}
			public SubClassModifierVerifier<SubClassModifierVerifier<?>> subClassModifiersFor(String subclassName) {
				Optional<ModifierDiff> dif = difference.getSubClassDifference(subclassName);
				assertThat(dif).isNotNull();
				
				return new SubClassModifierVerifier<SubClassModifierVerifier<?>>(this, dif.get());
			}
			
			public PARENT end() {
				return parent;
			}
		}
	}
}
