package uk.co.optimisticpanda.jarcompare;

import java.io.File;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import uk.co.optimisticpanda.jarcompare.diff.ClassDifferences;
import uk.co.optimisticpanda.jarcompare.diff.Differences;
import uk.co.optimisticpanda.jarcompare.util.JarReader;

import com.google.common.collect.Sets;

public class JarContents {

	private final SortedMap<String, ClassFile> contents = new TreeMap<>();

	public static JarContents load(File file){
		JarContents contents = new JarContents();
		JarReader.entriesOf(file)
					.stream(s -> s.map(ClassFile::new).forEach(contents::add));
		return contents;
	}
	
	private JarContents(){}
	
	private void add(ClassFile file){
		if(!file.getName().contains("$")){
			contents.put(file.getName(), file);
		}
	}
	
	public Differences difference(JarContents other){
		Set<String> removed = Sets.difference(contents.keySet(), other.contents.keySet());
		Set<String> added = Sets.difference(other.contents.keySet(), contents.keySet());
		
		return Differences.build()
					.add(new ClassDifferences(added, removed))
					.create();
	}
	
	public Stream<ClassFile> stream(){
		return contents.values().stream();
	}
}
