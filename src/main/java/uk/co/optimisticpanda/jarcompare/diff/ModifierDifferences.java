package uk.co.optimisticpanda.jarcompare.diff;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import uk.co.optimisticpanda.jarcompare.ClassFile;
import uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod;

public class ModifierDifferences {

	private Set<ModifierDiff> differences;

	public ModifierDifferences(SortedMap<String, ClassFile> contents,
			SortedMap<String, ClassFile> otherContents) {
		this.differences = new TreeSet<>(otherContents.keySet().stream()
			.filter(contents::containsKey)
			.map(key -> ModifierDiff.create(key, contents, otherContents))
			.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet()));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Class Modifier Difference:\n");
		for (ModifierDiff modifierDiff : differences) {
			builder.append("\t" + modifierDiff.key + ":\n");
			builder.append("\t\tbefore: " + modifierDiff.before+ "\n");
			builder.append("\t\tafter: " + modifierDiff.after+ "\n");
		}
		return builder.toString();
	}

	
	public static class ModifierDiff{
		
		public static Optional<ModifierDiff> create(String key, SortedMap<String, ClassFile> contents,
				SortedMap<String, ClassFile> otherContents){
			SortedSet<Mod> otherModifiers = otherContents.get(key).getModifiers();
			SortedSet<Mod> modifiers = contents.get(key).getModifiers();
			System.out.println(otherModifiers + "\t:\t" + modifiers + "\t:" + otherModifiers.equals(modifiers));
			if(otherModifiers.equals(modifiers)){
				return Optional.empty();
			}
			return Optional.of(new ModifierDiff(key, modifiers, otherModifiers));
		}
		
		private final String key;
		private final SortedSet<Mod> before;
		private final SortedSet<Mod> after;
		
		private ModifierDiff(String key, SortedSet<Mod> before, SortedSet<Mod> after ){
			this.key = key;
			this.before = before;
			this.after = after;
		}

		@Override
		public int hashCode() {
			return Objects.hash(key, before, after);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ModifierDiff other = (ModifierDiff) obj;
			return Objects.equals(key, other.key) 
					&& Objects.equals(after, other.after) 
					&& Objects.equals(before, other.before);
		}
	}
}
