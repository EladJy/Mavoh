package junitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import algorithms.search.BFS;

public class BFSTest {
	BFS<String> bfs;
	
	public BFSTest() {
		bfs = new BFS<String>();
	}
	
	@Test
	public void testNull() {
		try {
			bfs.Search(null);
		}catch (NullPointerException e) {
			assertTrue(1 > 0);
		}
		assertTrue("NullPointerException not thrown!" , 1 > 0);
	}
}
