package uk.co.optimisticpanda.jarcompare.test.util;

import java.io.File;

public class TestUtils {

	/**
	 * Gets files from the root of class path.
	 */
	public static File getFile(String name){
		return new File(TestUtils.class.getResource("/" + name).getFile());	
	}
	
}
