package uk.co.optimisticpanda.jarcompare.util;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;

public class PathNode {

	private String thisNode;
	private List<PathNode> children = new ArrayList<>();

	private PathNode(String root, List<List<String>> packages) {
		thisNode = root;

		Function<List<String>, List<String>> rest = list -> list.isEmpty() ? Collections
				.emptyList() : list.subList(1, list.size());

		Map<String, List<List<String>>> nodes = packages.stream()
				.filter(l -> !l.isEmpty())
				.collect(groupingBy(l -> l.get(0), mapping(rest, toList())));

		nodes.entrySet().stream()
				.map(e -> new PathNode(e.getKey(), e.getValue()))
				.forEach(children::add);
	}

	public static List<PathNode> newTree(Collection<Path> paths) {
		Map<String, List<List<String>>> nodes = paths.stream().collect(
				groupingBy(Path::first, mapping(Path::rest, toList())));

		List<PathNode> result = new ArrayList<>();
		for (Entry<String, List<List<String>>> entry : nodes.entrySet()) {
			result.add(new PathNode(entry.getKey(), entry.getValue()));
		}
		return result;
	}

	public String toString() {
		return toString(0) + "\n";
	}

	public String toString(int level) {
		List<String> parts = new ArrayList<>();
		String tabs = rangeClosed(0, level).mapToObj(i -> "  ").reduce("",
				String::concat);

		parts.add(tabs + thisNode);
		children.stream()
				.forEach(child -> parts.add(child.toString(level + 1)));
		return Joiner.on("\n").join(parts);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(thisNode, children);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathNode other = (PathNode) obj;
		return Objects.equal(children, other.children)
				&& Objects.equal(thisNode, other.thisNode);
	}

}