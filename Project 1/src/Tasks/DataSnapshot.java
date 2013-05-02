package Tasks;

/**
 * Stores a snapshot of the rolling data statistics
 */
public class DataSnapshot {
    private Map<String, int> testMap; 	// Using the Strings held as the keys for the int of the number of hits each word has gotten
	private String pageParsed; 			// Original page input to the parser
	private int wordCount; 				// Word count for all pages parsed
	private int urlCount;				// url count for all pages parsed
	private int pageCount;				// Number of pages parsed
	private int pageLimit;				// Max number of pages that can be parsed
	private long totalParseTime;		// Total time spent parsing pages
	
	public DataSnapshot(int pLimit, String pParsed) {		// Constructor for a datatype that holds all required output data
		testMap = new Map<String, int>();
		pageLimit = pLimit;
		pageParsed = pParsed;
		wordCount = 0;
		urlCount = 0;
		pageCount = 0;
		pageLimit = pLimit;
	}
	
	public DataSnapshot() {
		super(0, "");
	}
	
	public addWord (String word) {
		testMap.put(word, 0);
	}
	
	public void setWordCount (int count) {
		wordCount = count;
	}
	
	public void setPageCount(int count) {
		pageCount = count;
	}
	
	public void setPageLimit(int count) {
		pageLimit = count;
	}
	
	public void incURLCount () {
		urlCount++
	}
	
	public int getWordCount() {
		return wordCount;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	
	public int getPageLimit() {
		return pageLimit;
	}
	
	public int getURLCount() {
		return urlCount;
	}
}
