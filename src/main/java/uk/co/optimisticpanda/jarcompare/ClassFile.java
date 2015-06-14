package uk.co.optimisticpanda.jarcompare;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.SortedSet;

import javassist.CtClass;
import javassist.NotFoundException;
import uk.co.optimisticpanda.jarcompare.util.ModifierUtils;
import uk.co.optimisticpanda.jarcompare.util.ModifierUtils.Mod;

import com.google.common.base.Throwables;

public class ClassFile {

	private final String name;
	private SortedSet<Mod> modifiers;
	private List<ClassFile> children;

	public ClassFile(CtClass file) {
		this.name = file.getName();
		this.modifiers = ModifierUtils.getModifiers(file.getModifiers());
		try {
			this.children = stream(file.getNestedClasses()).map(ClassFile::new).collect(toList());
		} catch (NotFoundException e) {
			throw Throwables.propagate(e);
		}
	}

	public String getName() {
		return name;
	}

	public List<ClassFile> getNestedClasses() {
		return children;
	}

	public int getNestedClassesAndDescendentsSize() {
		return sizeOf(getNestedClasses());
	}

	public SortedSet<Mod> getModifiers() {
		return modifiers;
	}

	private int sizeOf(List<ClassFile> nestedFiles) {
		int count = 0;
		for (ClassFile child : nestedFiles) {
			count++;
			count += sizeOf(child.getNestedClasses());
		}
		return count;
	}
}
