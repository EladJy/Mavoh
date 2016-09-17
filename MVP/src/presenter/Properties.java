package presenter;

import java.io.Serializable;

public class Properties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 555913593004844041L;

	private String defaultAlgorithm;
	private int maxMazeSize;
	private String viewSetup;
	private int numberOfThreads;
	private String searchAlgorithm;
	
	public Properties(int numberOfThreads , String defaultAlgorithm , String searchAlgorithm , int maxMazeSize , String viewSetup) {
		super();
		this.numberOfThreads = numberOfThreads;
		this.defaultAlgorithm = defaultAlgorithm;
		this.viewSetup = viewSetup;
		this.maxMazeSize = maxMazeSize;
		this.searchAlgorithm = searchAlgorithm;
	}
	
	public Properties(Properties p) {
		defaultAlgorithm = p.defaultAlgorithm;
		maxMazeSize = p.maxMazeSize;
		numberOfThreads = p.numberOfThreads;
		searchAlgorithm = p.searchAlgorithm;
	}

	public Properties() {
		super();
	}
	public String getDefaultAlgorithm() {
		return defaultAlgorithm;
	}

	public void setDefaultAlgorithm(String defaultAlgorithm) {
		this.defaultAlgorithm = defaultAlgorithm;
	}

	public String getViewSetup() {
		return viewSetup;
	}

	public void setViewSetup(String viewSetup) {
		this.viewSetup = viewSetup;
	}

	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public String getSearchAlgorithm() {
		return searchAlgorithm;
	}

	public void setSearchAlgorithm(String searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
	}

	public int getMaxMazeSize() {
		return maxMazeSize;
	}

	public void setMaxMazeSize(int maxMazeSize) {
		this.maxMazeSize = maxMazeSize;
	}
}
