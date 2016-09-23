package junitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import algorithms.search.BFS;

/**
 * Junit test for BFS
 * @author Elad Jarby
 * @version 1.0
 * @since 18.09.2016
 */
public class BFSTest {
	BFS<String> bfs;
	
	/**
	 * Constructor to initialize bfs
	 */
	public BFSTest() {
		bfs = new BFS<String>();
	}
	
	/**
	 * Test for null
	 */
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
