package Tasks;

/**
 * Stores a snapshot of the rolling data statistics
 */
public class DataSnapshot {
    Map<String, int> testMap;
	String pageParsed;
	int wordCount;
	int urlCount;
	int pageCount;
	int pageLimit;
	long totalParseTime;
	
	
	public DataSnapshot(int pLimit, String pParsed) {
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
	
	public void incHitCount () {
		hitCount++;
	}
	
	public void setWordCount (int count) {
		wordCount = count;
	}
	
	public void setTestString(String test) {
		testString = test;
	}
	
	public void incURLCount () {
		urlCount++
	}
	
	public String getTestString() {
		return testString;
	}
	
	public int getHitCount() {
		return hitCount;
	}
	
	public int getWordCount() {
		return wordCount;
	}
	
	public int getURLCount() {
		return urlCount;
	}
}
