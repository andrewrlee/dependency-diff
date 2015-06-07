package uk.co.optimisticpanda.jarcompare.test.util;

import java.io.File;

import uk.co.optimisticpanda.jarcompare.JarContents;
import uk.co.optimisticpanda.jarcompare.diff.Differences;

public class TestUtils {

	/**
	 * Gets files from the root of class path.
	 */
	public static File getFile(String name) {
		return new File(TestUtils.class.getResource("/" + name).getFile());
	}

	public static DiffReturn differenceBetween(String filename1){
		return new DiffReturn(filename1);
	}
	
	public static class DiffReturn{
		private String filename1;

		private DiffReturn(String filename1) {
			this.filename1 = filename1;
		}

		public Differences and(String filename2){
			JarContents file1 = JarContents.load(getFile(filename1));
			JarContents file2 = JarContents.load(getFile(filename2));
			return file1.difference(file2);
		}
	}
}
