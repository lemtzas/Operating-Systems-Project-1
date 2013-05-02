package Tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores a snapshot of the rolling data statistics
 */
public class DataSnapshot {
    public Map<String, Integer> testMap; 	// Using the Strings held as the keys for the int of the number of hits each word has gotten
    public String pageParsed; 			// Original page input to the parser
    public int wordCount; 				// Word count for all pages parsed
    public int urlCount;				// url count for all pages parsed
    public int pageCount;				// Number of pages parsed
    public int pageLimit;				// Max number of pages that can be parsed
    public long totalParseTime;		// Total time spent parsing pages
    public long totalRunTime;		// Total time spent parsing pages
	
	public DataSnapshot(int pLimit, String pParsed) {		// Constructor for a datatype that holds all required output data
		testMap = new HashMap<String, Integer>();
		pageLimit = pLimit;
		pageParsed = pParsed;
		wordCount = 0;
		urlCount = 0;
		pageCount = 0;
		pageLimit = pLimit;
        totalParseTime = 0;
        totalRunTime = 0;
	}
	
	public DataSnapshot() {
		this(0, "");
	}

    public DataSnapshot(DataSnapshot other) {
        this.testMap = (HashMap<String,Integer>)((HashMap<String,Integer>)other.testMap).clone();

        //straight copy
        this.pageParsed = other.pageParsed;
        this.wordCount = other.wordCount;
        this.urlCount = other.urlCount;
        this.pageCount = other.pageCount;
        this.pageLimit = other.pageLimit;
        this.totalParseTime = other.totalParseTime;
        this.totalRunTime = other.totalRunTime;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        String print = "%-15s\t%2.5f\t\t\t\t%d";

        sb.append("Parsed: ").append(pageParsed).append("\n");
        sb.append("Pages Retrieved: ").append(pageCount).append("\n");
        sb.append("\tKeyword\t\tAve. hits per page\t\tTotal hits\n");
        for(String s : testMap.keySet())
            sb.append("  ").append(String.format(print,s,testMap.get(s)/(double)pageCount,testMap.get(s))).append("\n");
        sb.append("Page Limit: ").append(pageLimit).append("\n");
        sb.append("Average Parse time per page: ").append(totalParseTime / (double) pageCount / 1000d).append("\n");
        sb.append("Total running time: ").append(totalRunTime / 1000d).append("\n");

        return sb.toString();
    }
}
