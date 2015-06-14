package uk.co.optimisticpanda.jarcompare.diff;

import java.io.File;

import uk.co.optimisticpanda.jarcompare.JarContents;

public class Differences {

	private ClassDifferences classDifference;
	private ClassModifierDifferences classModifierDifference;
	
	public static Builder build(){
		return new Builder();
	}
	
	public static DiffReturn differenceBetween(String filename1){
		return new DiffReturn(new File(filename1));
	}
	
	public static DiffReturn differenceBetween(File filename1){
		return new DiffReturn(filename1);
	}
	
	private Differences(ClassDifferences classDifference, ClassModifierDifferences classModifierDifference){
		this.classDifference = classDifference;
		this.classModifierDifference = classModifierDifference;
	}

	public ClassDifferences getClassDifference(){
		return classDifference;
	}
	
	public ClassModifierDifferences getClassModifierDifference() {
		return classModifierDifference;
	}
	
	@Override
	public String toString() {
		return "Differences:\n" + classDifference + "\n" + classModifierDifference;
	}

	public static class Builder{
		
		private ClassDifferences classDifference;
		private ClassModifierDifferences classModifierDifference;
		
		public Builder add(ClassDifferences classDifferences){
			classDifference = classDifferences;
			return this;
		}
		
		public Builder add(ClassModifierDifferences classModifierDifferences){
			classModifierDifference = classModifierDifferences;
			return this;
		}
		
		public Differences create(){
			return new Differences(classDifference, classModifierDifference);
		}
	}
	
	public static class DiffReturn{
		private File filename1;

		private DiffReturn(File file) {
			this.filename1 = file;
		}
		public Differences and(File filename2){
			JarContents file1 = JarContents.load(filename1);
			JarContents file2 = JarContents.load(filename2);
			return file1.difference(file2);
		}
		public Differences and(String filename2){
			return and(new File(filename2));
		}
	}

}
