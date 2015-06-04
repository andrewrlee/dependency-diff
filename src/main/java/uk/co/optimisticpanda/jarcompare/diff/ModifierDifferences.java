package uk.co.optimisticpanda.jarcompare.diff;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.IntStream;

import uk.co.optimisticpanda.jarcompare.ClassFile;
import uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod;
public class ModifierDifferences {

	private Set<ModifierDiff> differences;

	public ModifierDifferences(SortedMap<String, ClassFile> contents,
			SortedMap<String, ClassFile> otherContents) {
		this.differences = new TreeSet<>(otherContents.keySet().stream()
				.filter(contents::containsKey)
				.map(key -> ModifierDiff.create(key, contents, otherContents))
				.filter(Optional::isPresent).map(Optional::get)
				.collect(toSet()));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Class Modifier Difference:\n");
		for (ModifierDiff modifierDiff : differences) {
			builder.append(modifierDiff);
		}
		return builder.toString();
	}

	public static class ModifierDiff implements Comparable<ModifierDiff> {

		public static Optional<ModifierDiff> create(String key,
				SortedMap<String, ClassFile> contents,
				SortedMap<String, ClassFile> otherContents) {

			ClassFile afterVersion = otherContents.get(key);
			ClassFile beforeVersion = contents.get(key);

			ModifierDiff modifierDiff = new ModifierDiff(beforeVersion,
					afterVersion);

			if (!modifierDiff.isDifferent()) {
				return Optional.empty();
			}
			return Optional.of(modifierDiff);
		}

		private final String key;
		private final SortedSet<Mod> before;
		private final SortedSet<Mod> after;
		private final List<ModifierDiff> children;

		private ModifierDiff(ClassFile beforeVersion,
				ClassFile afterVersion) {
			this.key = beforeVersion.getName();

			this.children = getChildren(beforeVersion.getNestedClasses(),
					afterVersion.getNestedClasses());
			this.before = beforeVersion.getModifiers();
			this.after = afterVersion.getModifiers();
		}

		private List<ModifierDiff> getChildren(
				List<ClassFile> beforeVersionClasses,
				List<ClassFile> afterVersionClasses) {

			Function<ClassFile, Pair<ClassFile, ClassFile>> matchingClass = 
					ov -> {
						return afterVersionClasses.stream()
								.filter(nv -> nv.getName().equals(ov.getName()))
								.findFirst()
								.map(nv -> new Pair<ClassFile, ClassFile>(ov, nv)).orElse(null);	
					};

			return beforeVersionClasses.stream()
					.map(matchingClass)
					.filter(p -> p != null)
					.map(pair -> new ModifierDiff(pair.left, pair.right)).collect(toList());
		}

		public static class Pair<L, R>{
			
			private L left;
			private R right;

			public Pair(L left, R right){
				this.left = left;
				this.right = right;
			}
		}
		
		private boolean isDifferent() {
			return isDifferent(this);
		}

		private boolean isDifferent(ModifierDiff diff) {
			if (!before.equals(after)) {
				return true;
			}
			for (ModifierDiff mod : children) {
				if (mod.isDifferent()) {
					return true;
				}
			}
			return false;
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

		@Override
		public int compareTo(ModifierDiff o) {
			return key.compareTo(o.key);
		}
		
		@Override
		public String toString() {
			return toString(1);
		}

		public String toString(int level) {
			StringBuilder builder = new StringBuilder();
			String tabs = IntStream.rangeClosed(0, level).mapToObj(i -> "\t").reduce("", String::concat);
			builder.append(tabs + key + ":\t" + before + " -> " + after + "\n");
			children.stream().forEach(child -> builder.append(child.toString(level+ 1)));
			return builder.toString();
		}

	
	}
}
