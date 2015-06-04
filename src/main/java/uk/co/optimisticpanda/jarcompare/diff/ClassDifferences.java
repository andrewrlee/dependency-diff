package uk.co.optimisticpanda.jarcompare.diff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import uk.co.optimisticpanda.jarcompare.ClassFile;

import com.google.common.collect.Sets;

public class ClassDifferences {

	private final SortedSet<String> additions = new TreeSet<>();
	private final SortedSet<String> removals = new TreeSet<>();
	private final ModifierDifferences modifierDifferences;
	
	public ClassDifferences(SortedMap<String, ClassFile> contents, SortedMap<String, ClassFile> otherContents) {
		Set<String> removed = Sets.difference(contents.keySet(), otherContents.keySet());
		Set<String> added = Sets.difference(otherContents.keySet(), contents.keySet());
		
		this.additions.addAll(added);
		this.removals.addAll(removed);
		this.modifierDifferences = new ModifierDifferences(contents, otherContents);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Class Differences:\n");
		builder.append("\tAdditions:\n");
		additions.forEach(a -> builder.append("\t\t* " + a + "\n"));
		builder.append("\tRemovals:\n");
		removals.forEach(a -> builder.append("\t\t* " + a + "\n"));
		builder.append(modifierDifferences);
		return builder.toString();
	}

	public boolean hasAddition(String name) {
		return additions.contains(name);
	}
	
	public boolean hasRemoval(String name) {
		return removals.contains(name);
	}

	public int getAdditionSize() {
		return additions.size();
	}
	
	public int getRemovalSize() {
		return removals.size();
	}
}
