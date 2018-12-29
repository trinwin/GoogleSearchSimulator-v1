/**
 * The GoogleSearch Program implements an application that
 *  - search for a keyword and display the top 10 websites with randomly
 *  generated scores using max heap priority queue.
 *  - increase priority of a website
 *  - display top 10 unique search keywords
 *
 * @author  Trinh Nguyen
 * @class   CS146-07
 * @version 1.0
 * @date    2018-10-20
 */


/**
 * This is the main method which call function Searching, which
 * Searching() method perform search with keyword input and display top
 * 10 websites and allow user to increase priority of the website of user
 * choice and display the top 10 unique search keywords.
 * @param args - Unused
 * @return Nothing.
 * @exception Exception on Heap Underflow or if the new total score is smaller than current one
 * @time_complexity O(nlgn)
 */

public class Main {
    public static void main(String[] args) throws Exception {
        GoogleSearch google = new GoogleSearch();
        google.Searching(); //O(nlgn)
    }
}
