package de.peeeq.jmpq;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class TestMpq {

	File map = new File("map.w3m");
	
	
	@Test
	public void testExtract() throws IOException {
		File test = new File("test1.w3m");
		Files.copy(map, test);

		File temp = new File("temp.txt");
		temp.delete();
		
		new Main(new String[] {"extract", test.getAbsolutePath(), "war3map.j", temp.getAbsolutePath()});
		
		String s = Files.toString(temp, Charsets.UTF_8);
		
		assertTrue(s.contains("function main"));
	}
	
	// TODO more tests

}
