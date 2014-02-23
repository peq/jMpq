package de.peeeq.jmpq;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class TestMpq {

	File map = new File("map.w3m");
	File map2 = new File("map2.w3x");
	
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
	
	@Test
	public void testRemoveAddExtract() throws IOException, InterruptedException {
		File test = new File("test2.w3m");
		Files.copy(map2, test);

		File temp = new File("temp.w3u");
		File temp2 = new File("temp2.w3u");
		File temp3 = new File("temp3.w3u");
		temp.delete();
		temp2.delete();
		temp3.delete();
		
		
		Files.write("Hello world", temp2, Charsets.UTF_8);
		
		// extract w3u:
		new Main(new String[] {"extract", test.getAbsolutePath(), "war3map.w3u", temp.getAbsolutePath()});
		
		new Main(new String[] {"delete", test.getAbsolutePath(), "war3map.w3u"});
		new Main(new String[] {"compact", test.getAbsolutePath()});
		
		
		new Main(new String[] {"add", test.getAbsolutePath(), temp2.getAbsolutePath(), "war3map.w3u"});
		
		
		new Main(new String[] {"extract", test.getAbsolutePath(), "war3map.w3u", temp3.getAbsolutePath()});
		
		String s = Files.toString(temp3, Charsets.UTF_8);
		
		assertTrue(s.contains("Hello world"));
	}

	
	@Test
	public void testOverwrite() throws IOException, InterruptedException {
		File test = new File("test2.w3m");
		Files.copy(map2, test);

		File temp = new File("temp.w3u");
		File temp2 = new File("temp2.w3u");
		File temp3 = new File("temp3.w3u");
		temp.delete();
		temp2.delete();
		temp3.delete();
		
		
		Files.write("Hello world", temp2, Charsets.UTF_8);
		
		// extract w3u:
		new Main(new String[] {"extract", test.getAbsolutePath(), "war3map.w3u", temp.getAbsolutePath()});
		
		new Main(new String[] {"add", test.getAbsolutePath(), temp2.getAbsolutePath(), "war3map.w3u"});
		
		
		new Main(new String[] {"extract", test.getAbsolutePath(), "war3map.w3u", temp3.getAbsolutePath()});
		
		String s = Files.toString(temp3, Charsets.UTF_8);
		
		assertTrue(s.contains("Hello world"));
	}
	
}
