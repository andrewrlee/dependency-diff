package uk.co.optimisticpanda.jarcompare;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import uk.co.optimisticpanda.jarcompare.diff.ClassDifferences;
import uk.co.optimisticpanda.jarcompare.diff.ClassModifierDifferences;
import uk.co.optimisticpanda.jarcompare.diff.Differences;
import uk.co.optimisticpanda.jarcompare.util.JarReader;
import uk.co.optimisticpanda.jarcompare.util.Path;
import static uk.co.optimisticpanda.jarcompare.util.Path.*;

public class JarContents {

	private final SortedMap<Path, ClassFile> contents = new TreeMap<>();

	public static JarContents load(File file) {
		JarContents contents = new JarContents();
		JarReader.classes(file).forEach(contents::add);
		return contents;
	}

	private JarContents() {
	}

	private void add(ClassFile file) {
		contents.put(newPath(file.getName()), file);
	}

	public Differences difference(JarContents other) {
		return Differences.build()
				.add(new ClassDifferences(contents, other.contents))
				.add(new ClassModifierDifferences(contents, other.contents))
				.create();
	}

	public Stream<ClassFile> stream() {
		return contents.values().stream();
	}
}
