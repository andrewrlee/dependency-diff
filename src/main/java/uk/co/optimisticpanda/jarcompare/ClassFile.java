package uk.co.optimisticpanda.jarcompare;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import javassist.CtClass;
import javassist.NotFoundException;
import uk.co.optimisticpanda.jarcompare.AccessFlagUtils.Flag;
import uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod;

import com.google.common.base.Throwables;

public class ClassFile {

	private final String name;
	private SortedSet<Mod> modifiers;
	private List<ClassFile> children;

	public ClassFile(CtClass file) {
		
		this.name = file.getName();
		this.modifiers = ModifierUtils.getModifiers(file.getModifiers());

		if (name.contains("ClassF")) {
			SortedSet<Flag> flags = AccessFlagUtils.getFlags(file
					.getClassFile().getAccessFlags());
			System.out.println(file.getClassPool().hashCode() + "\t" + name + ": " + modifiers);
		}

		try {
			this.children = Arrays.stream(file.getNestedClasses())
					.map(ClassFile::new).collect(Collectors.toList());
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
