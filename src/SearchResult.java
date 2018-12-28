/**
 * SearchResult class
 *  - stores the data of a website (Title, URL, PageRank object)
 *  - has constructor, setters and getters to access the private variables,
 * and print the value of the object.
 */

public class SearchResult {
    private String title;
    private String url;
    private PageRank score;

    /** Default Constructor */
    public SearchResult(){
        PageRank newScore = new PageRank();
        newScore.generateScore();
        this.score = newScore;
    }

    /** Setters */
    public void setTitle(String title) { this.title = title; }

    public void setUrl(String url) { this.url = url; }

    public void setScores(PageRank score) { this.score = score; }

    public void setOneScore(int index, int value) { this.score.setOneScore(index, value); }

    public void setTotalScore(int total) { this.score.setTotalScore(total); }

    /** Getters */
    public String getTitle() { return title; }

    public String getUrl() { return url; }

    public PageRank getScores() { return score; }

    public int getTotalScore() { return score.getTotalScore(); }

    /**
     * printOne() method is used StringBuffer to display one object
     * SearchResult (Website title, url, each factor, and totalscore
     * @param none
     * @return none
     * @time_complexity O(n)
     */
    public void printOne(){
        StringBuffer buff = new StringBuffer();
        buff.append("Title: ").append(title).append(", ");
        buff.append("\nURL: ").append(url);//.append("\n");
        System.out.print(buff);
        score.print(); //O(n)
    }

    /**
     * printAll() method is used StringBuffer to display all object
     * SearchResult (Website title, url, each factor, and totalscore
     * @param rank used to display the position of object on the list
     * @return none
     * @time_complexity O(n)
     */
    public void printAll(int rank){
        StringBuffer buff = new StringBuffer();
        buff.append(rank).append(".");
        buff.append(" Title: ").append(title).append(", ");
        buff.append("\n   URL: ").append(url);
        System.out.print(buff);
        score.print(); //O(n)
    }
}
