package Tasks;

/**
 * Stores a snapshot of the rolling data statistics
 */
public class DataSnapshot {
    Map<String, int> testMap; 	// Using the Strings held as the keys for the int of the number of hits each word has gotten
	String pageParsed; 			// Original page input to the parser
	int wordCount; 				// Word count for all pages parsed
	int urlCount;				// url count for all pages parsed
	int pageCount;				// Number of pages parsed
	int pageLimit;				// Max number of pages that can be parsed
	long totalParseTime;		// Total time spent parsing pages
	
	
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
	
	public void setWordCount (int count) {
		wordCount = count;
	}
	
	public void incURLCount () {
		urlCount++
	}
	
	public int getWordCount() {
		return wordCount;
	}
	
	public int getURLCount() {
		return urlCount;
	}
}
