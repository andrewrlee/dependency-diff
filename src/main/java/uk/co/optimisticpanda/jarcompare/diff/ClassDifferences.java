package uk.co.optimisticpanda.jarcompare.diff;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import uk.co.optimisticpanda.jarcompare.ClassFile;
import uk.co.optimisticpanda.jarcompare.util.Path;
import uk.co.optimisticpanda.jarcompare.util.PathNode;

import com.google.common.collect.Sets;

public class ClassDifferences {

	private final SortedSet<Path> additions = new TreeSet<>();
	private final SortedSet<Path> removals = new TreeSet<>();
	private final ClassModifierDifferences modifierDifferences;
	
	public ClassDifferences(SortedMap<Path, ClassFile> contents, SortedMap<Path, ClassFile> otherContents) {
		Set<Path> removed = Sets.difference(contents.keySet(), otherContents.keySet());
		Set<Path> added = Sets.difference(otherContents.keySet(), contents.keySet());
		Set<Path> same = Sets.intersection(contents.keySet(), otherContents.keySet());
		this.additions.addAll(added);
		this.removals.addAll(removed);
		this.modifierDifferences = new ClassModifierDifferences(contents, otherContents);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Class Differences:\n");
		builder.append("\tAdditions:\n");
		PathNode.newTree(additions).forEach(builder::append);
		builder.append("\tRemovals:\n");
		PathNode.newTree(removals).forEach(builder::append);
		builder.append(modifierDifferences);
		return builder.toString();
	}

	public boolean hasAddition(String name) {
		return additions.contains(Path.newPath(name));
	}
	
	public boolean hasRemoval(String name) {
		return removals.contains(Path.newPath(name));
	}

	public int getAdditionSize() {
		return additions.size();
	}
	
	public int getRemovalSize() {
		return removals.size();
	}
}
