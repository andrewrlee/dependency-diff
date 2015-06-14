package uk.co.optimisticpanda.jarcompare.diff;

public class Differences {

	private ClassDifferences classDifference;
	private ClassModifierDifferences classModifierDifference;
	
	public static Builder build(){
		return new Builder();
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
}
