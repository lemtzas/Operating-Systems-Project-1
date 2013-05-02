Operating-Systems-Project-1
===========================
## Command Line Options ##
```
-s : Single-threaded single-pool mode
-m <threads> : multi-threaded single-pool mode
-d <retriever_threads> <parser_threads> : multi-threaded single-pool mode
-url <url> : specify url
-keywords <keywords> : specify keywords
-max <pages> : specify the maximum number of pages
```

A web crawler, meant to test our knowledge of threading system.s

![](http://faculty.washington.edu/gmobus/Academics/TCSS422/MoodleFiles/WebSpider.png)

Output Format:
```
Parsed: www.tacoma.washington.edu/calendar/
Pages Retrieved: 12
Average words per page: 321
Average URLs per page: 11
Keyword               Ave. hits per page       Total hits
  albatross               0.001                     3
  carrots                 0.01                      5
  everywhere              1.23                      19
  etc..........

  intelligence            0.000                     0

Page limit: 5000
Average parse time per page .001msec
Total running time:       0.96 sec
```

## Class Design ##
### UI ###
Should initialize and provide a method to display reported stats as well as dump the text to a file `collected_data.txt`

> 

### Reporter ###
Places output buffer results onto screen. Pops a data structure off the output queue and sends it to the UI.

### DataGatherer / ThreadPoolManager ###
I'm not sure on this.
<del>Manages the threads and such.
```java
public class ThreadPoolManager {
    
    /*Add rootURL to the pageToRetreiveQueue*/
    public ThreadPoolManager(int threads, URL rootURL);
    /*Construct all threads.*/
    public void begin();
    private class PageParserRunner implements Runnable{}
    private class PageRetrieverRunner implements Runnable{}
    private class DataGathererRunner implements Runnable{}
}
```
</del>

### PageParser ###
One-use class that immediately parses a page's text and generates stats.
```Java
public class PageParser {
    /*Parse the html given.*/
    public PageParser(String text, Set<String> wordsToCount);
    /*What links were found in the text?*/
    public URL[] getFoundURLs();
    /*Word Count*/
    public int getWordCount();
    /*Stats for wordsToCount*/
    public Map<String,Integer> getWordStats();
}
```

### PageRetriever ###
One-use class that immediately grabs a web page's text.
```Java
public class PageRetriever {
    /*Retrieve the HTML*/
    public PageRetriever(URL url);
    /*The text found*/
    public String getText();
}
```

http://faculty.washington.edu/gmobus/
