package uk.co.optimisticpanda.jarcompare;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javassist.CtClass;
import javassist.NotFoundException;
import uk.co.optimisticpanda.jarcompare.ModifierUtils.Mod;

import com.google.common.base.Throwables;

public class ClassFile {

	private final String name; 
	private SortedSet<Mod> modifiers;
	private CtClass file;
	
	public ClassFile(CtClass file){
		this.file = file;
		this.name = file.getName();
		if(name.contains("ClassC")){
			System.out.println("asas");
		}
		this.modifiers = ModifierUtils.getModifiers(file.getModifiers());
		
		System.out.println(file.getName() + " \t : " + modifiers);
	}
	
	public String getName() {
		return name;
	}
	
	public List<ClassFile> getNestedClasses(){
		try {
			return Arrays.stream(file.getNestedClasses()).map(ClassFile::new).collect(Collectors.toList());
		} catch (NotFoundException e) {
			throw Throwables.propagate(e);
		}
	}
	
		public int getNestedClassesAndDescendentsSize(){
		return sizeOf(getNestedClasses());
	}

	public SortedSet<Mod> getModifiers() {
		return modifiers;
	}
	
	private int sizeOf(List<ClassFile> nestedFiles){
		int count = 0;
		for (ClassFile child : nestedFiles) {
			count++;
			count += sizeOf(child.getNestedClasses());
		}
		return count;
	} 
	
	public Stream<ClassFile> streamNestedClasses(){
		return getNestedClasses().stream();
	}
}
