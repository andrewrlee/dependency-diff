package uk.co.optimisticpanda.jarcompare.diff;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ClassDifferences {

	private SortedSet<String> additions = new TreeSet<>();
	private SortedSet<String> removals = new TreeSet<>();

	public ClassDifferences(Set<String> additions, Set<String> removals) {
		this.additions.addAll(additions);
		this.removals.addAll(removals);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Class Differences:\n");
		builder.append("\tAdditions:\n");
		additions.forEach(a -> builder.append("\t\t* " + a + "\n"));
		builder.append("\tRemovals:\n");
		removals.forEach(a -> builder.append("\t\t* " + a + "\n"));
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
