package uk.co.optimisticpanda.jarcompare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javassist.CtClass;
import javassist.NotFoundException;

import com.google.common.base.Throwables;

public class ClassFile {

	final String name; 
	final List<ClassFile> nestedClasses = new ArrayList<>();
	
	public ClassFile(CtClass file){
		this.name = file.getName();
		try {
			Arrays.stream(file.getNestedClasses()).forEach(nc -> nestedClasses.add(new ClassFile(nc)));
		} catch (NotFoundException e) {
			throw Throwables.propagate(e);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getNestedClassesAndDescendentsSize(){
		return sizeOf(nestedClasses);
	}
	
	private int sizeOf(List<ClassFile> nestedFiles){
		int count = 0;
		for (ClassFile child : nestedFiles) {
			count++;
			count += sizeOf(child.nestedClasses);
		}
		return count;
	} 
	
	public Stream<ClassFile> streamNestedClasses(){
		return nestedClasses.stream();
	}
}
