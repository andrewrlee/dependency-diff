package uk.co.optimisticpanda.jarcompare.diff;

public class Differences {

	private ClassDifferences classDifference;
	
	public static Builder build(){
		return new Builder();
	}
	
	private Differences(ClassDifferences classDifference){
		this.classDifference = classDifference;
	}
	
	@Override
	public String toString() {
		return "Differences:\n" + classDifference;
	}

	public static class Builder{
		
		private ClassDifferences classDifference;
		
		public Builder add(ClassDifferences classDifferences){
			classDifference = classDifferences;
			return this;
		}
		
		public Differences create(){
			return new Differences(classDifference);
		}
	}
}
