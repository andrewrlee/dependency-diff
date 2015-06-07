package uk.co.optimisticpanda.jarcompare.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;

public class Path implements Comparable<Path>{

	private List<String> segments = new ArrayList<>();
	
	private Path() {
	}

	public static Path newPath(String segment){
		return new Path().addAll(Splitter.on("$").splitToList(segment));
	}
	
	public Path add(String segment){
		segments.add(segment);
		return this;
	}
	
	public Path addAll(List<String> segs){
		segments.addAll(segs);
		return this;
	}
	
	public boolean notEmpty(){
		return !segments.isEmpty();
	}
	
	public List<String> get(){
		return segments.stream()
				.map(Splitter.on(".")::splitToList)
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}
	
	public List<String> rest(){
		return segments.stream()
				.map(Splitter.on(".")::splitToList)
				.flatMap(List::stream)
				.skip(1)
				.collect(Collectors.toList());
	}
	
	
	public String first(){
		return get().get(0);
	} 
	
	@Override
	public String toString() {
		return segments.stream().reduce("", (a, b) -> a + " -> " + b);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(segments);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		return Objects.equal(segments, other.segments);
	}

	@Override
	public int compareTo(Path o) {
		return toString().compareTo(o.toString());
	}
}
