package uk.co.optimisticpanda.jarcompare.util;

import static uk.co.optimisticpanda.jarcompare.test.util.TestUtils.getFile;

import java.io.File;

import org.junit.Test;

public class JarReaderTest {

	private static final File VERSION_0_0_1_FILE = getFile("test-0.0.1-SNAPSHOT.jar");

	@Test
	public void check() {
		JarReader.entriesOf(VERSION_0_0_1_FILE);
	}

	public static void main(String[] args) throws InterruptedException {
		JarReader.packages(VERSION_0_0_1_FILE)
				.subscribe(System.out::println);
	}
}
