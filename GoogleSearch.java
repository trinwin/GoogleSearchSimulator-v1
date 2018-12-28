import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * OneSearch class
 *  - stores the data of all searches (list of all searches and heap_size)
 *  - constructor, setters and getters to access the private variables,
 * and print the value of the object.
 *  - have heap and max heap function to build a priority queue of top 10 unique keywords.
 *  - allows user to perform search and increase website priority
 *  - function that validate user input
 */

public class GoogleSearch {
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private ArrayList<OneSearch> SearchList = new ArrayList<>();
    public int heap_size;

    /** Default Constructor */
    public GoogleSearch() { }

    /** Setters */
    private void setSearchList(ArrayList<OneSearch> searchList) { SearchList = searchList; }

    private void setHeap_size(int heap_size) { this.heap_size = heap_size -1; }

    /** Getters */
    private ArrayList<OneSearch> getSearchList() { return SearchList; }

    private int getHeap_size() { return heap_size; }

    /**------------------------------- Heap Functions -------------------------------*/

    /**
     * Parent() method returns index of the parent of node with index i.
     * @param i - index of node
     * @return  index of the parent of node with index i
     * @time_complexity O(1)
     */
    private int Parent(int i){ return i/2; }

    /**
     * Left() method returns index of the left child of node with index i.
     * @param i - index of node
     * @return  index of the left child of node with index i
     * @time_complexity O(1)
     */
    private int Left(int i){ return 2*i; }

    /**
     * Right() method returns index of the right child of node with index i.
     * @param i - index of node
     * @return  index of the right child of node with index i
     * @time_complexity O(1)
     */
    private int Right(int i){ return (2*i)+1; }

    /**
     * Searching() method perform search with keyword input and display top
     * 10 websites and allow user to increase priority of the website of user
     * choice and display the top 10 unique search keywords.
     * @param none
     * @return  none
     * @exception Exception Heap Underflow or if the new total score is smaller than current one
     * @time_complexity O(nlgn)
     */
    public void Searching() throws Exception {
        Scanner scanner = new Scanner(System.in);
        int searchIndex = 0;
        String repeat;
        //Set the first object to null (index 0) for the index to start at 1
        SearchList.add(searchIndex, null);
        int numResult = 30;
        do {    //O(n)
            OneSearch oneSearch = new OneSearch();
            ArrayList<SearchResult> resultList = new ArrayList<SearchResult>(); //Create temp object of ArrayList of SearchResult
            resultList.add(0, null);  //Add the first null element index 0
            String searchKeyword = "";
            System.out.print("Please enter the search term: ");
            if (scanner.hasNextLine())
                searchKeyword = scanner.nextLine();

            String searchURL = GOOGLE_SEARCH_URL + "?q=" + searchKeyword + "&num=" + numResult;
            //without proper User-Agent, we will get 403 error
            Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

            //If google search results HTML change the <h3 class="r" to <h3 class="r1"
            //we need to change below accordingly
            Elements results = doc.select("h3.r > a");

            //ArrayList starts at 1
            int index = 1;
            for (Element result : results) {
                SearchResult tempSearchResult = new SearchResult();
                String url = result.attr("href"); //Extract the URL
                String title = result.text();       //Extract the title
                url = url.substring(7, url.indexOf("&")); //modify String to get only the URL (Change 6-7)
                tempSearchResult.setTitle(title);   //Add title of website to object Search
                tempSearchResult.setUrl(url);       //Add url of website to object Search
                resultList.add(index, tempSearchResult);    //Add SearchResult object to ArrayList
                index++;    //Increment index to add the next object to ArrayList
            }
            //Set SearchKeyword and ArrayList of SearchResult object of OneSearch object
            oneSearch.setList(resultList);
            oneSearch.setKeyword(searchKeyword);
            oneSearch.setCounter(1);


            //Check whether the keyword already existed
            int keywordCheck = processKeyword(searchKeyword.trim());    //O(n)
            searchIndex++;

            //Add new OneSearch object to ArrayList of OneSearch
            //BuildMaxHeap and HeapSort --> Priority Queue
            searchIndex = DisplayQueue(oneSearch, searchIndex, keywordCheck); //O(nlgn)

            System.out.print("Would you like to do another search (Yes/No)? --> ");
            repeat = scanner.nextLine();
            scanner.reset();
        }while (repeat.trim().equalsIgnoreCase("yes") );

        Menu(scanner);  //O(nlgn)
        scanner.close();
    }

    /**
     * DisplayQueue() method add new OneSearch object to the list, build and display
     * a priority queue of top 10 websites. Remove the object if it already existed
     * in the list and return the index to add next object.
     * @param   oneSearch - new object to be added to array list
     * @param   searchIndex - index of new object in array list
     * @param   action - action performed if search keyword already existed or not in the array list
     * @return  searchIndex - to add the new object to list
     * @exception Exception on Heap Underflow
     * @time_complexity O(nlgn)
     */
    private int DisplayQueue(OneSearch oneSearch, int searchIndex, int action) throws Exception {
        //Add new OneSearch object to ArrayList of OneSearch
        SearchList.add(searchIndex, oneSearch);     //O(1)
        //Display list of 30 found website
        //SearchList.get(searchIndex).printList();    //O(n)
        //Build the Max Heap of ArrayList<OneSearch> SearchList
        SearchList.get(searchIndex).BuildMaxHeap(); //O(n)
        //DO NOT CALL HeapSort BEFORE CALLING PriorityQueue
        //Create a priority queue of top 10 websites based on their total score
        SearchList.get(searchIndex).PriorityQueue();  //O(nlgn)
        //Display a priority queue of top 10 websites
        SearchList.get(searchIndex).printList();    //O(n)
        if (action==1) {  //if the keyword already existed
            //Display top 10 websites - Remove it from the list since the keyword existed
            SearchList.remove(searchIndex);
            //The size and index decrease and return current index-1 for adding the next object to array list
            return searchIndex-1;
        }
        //return current index for adding the next object to array list
        return searchIndex;
    }

    /**
     * Menu() method let user chooses between increase website priority or
     * display the top 10 unique search keywords.
     * @param   scanner
     * @return  none
     * @exception Exception if the new total score is smaller than current one
     * @time_complexity O(nlgn)
     */
    private void Menu(Scanner scanner) throws Exception {
        String mess =  "\nWhat would you like to do?" +
                "\nOption 1: Increase Priority of your Website" +
                "\nOption 2: Display top 10 unique search keyword" +
                "\nOption 3: Exit" +
                "\nEnter the option (1 or 2): ";
        //Get user choice (1 or 2) (with validation)
        String repeat;
        int option = UserOptionInputValidation(mess, scanner); //O(n)
        switch (option) {
            case 1:
                ModifyRequest(scanner); //O(nlgn)
            case 2:
                DisplayTopKeyword();    //O(nlgn)
            case 3:
                System.exit(0);
            default:
                System.out.println("Error: Input must be a positive integer (1-3).");
        }
    }

    /**
     * DisplayTopKeyword() method build a MaxHeap of search keywords with
     * their occurrence counter and display the top 10 unique ones.
     * @param none
     * @return  none
     * @time_complexity O(nlgn)
     */
    private void DisplayTopKeyword(){
        //Build Max Heap of ArrayList<OneSearch> SearchList
        BuildMaxHeap(); //O(n)
        //Sort the elements by their counter (number of keyword occurrence)
        HeapSort();     //O(nlgn)
        //Display the list of top 10 unique keywords (highest to lowest occurrence)
        printKeyword(); //O(n)
    }

    /**
     * ModifyRequest() method takes in user input to change one factor score
     * of a Website to increase its priority and then display the updated heap.
     * @param scanner - scan user input
     * @return  none
     * @exception Exception if the new total score is smaller than current one
     * @time_complexity O(nlgn)
     */
    //Use the same array instead of creating new one
    private void ModifyRequest(Scanner scanner) throws Exception {
        System.out.println("\n--------------- INCREASE PRIORITY ---------------");
        //Get the SearchResult object to modify (with input validation)
        int nodeIndex = UserNodeInputValidation("Enter number of website (1-10): ", scanner); //O(n)
        //Display SearchResult object that user chose
        SearchList.get(1).getList().get(nodeIndex).printOne();
        //Get the factor to modify score (with input validation)
        int factorIndex = UserFactorInputValidation("Enter number of factor (1-4): ", scanner); //O(n)
        //Display current score of that factor
        System.out.print("Current score of Factor " + factorIndex + ": ");
        int currentScore = SearchList.get(1).getList().get(nodeIndex).getScores().getOneScore(factorIndex - 1);
        System.out.print(currentScore);
        //Get the new score that user want to update (with input validation)
        int newScore = UserScoreInputValidation("\nEnter new score: ", currentScore, scanner); //O(n)

        //Modify the factor score
        SearchList.get(1).getList().get(nodeIndex).getScores().setOneScore(factorIndex - 1, newScore);
        //Recalculate the total score
        SearchList.get(1).getList().get(nodeIndex).getScores().calTotalScore();
        //Print the SearchResult object with updated scores
        SearchList.get(1).getList().get(nodeIndex).printOne();
        //Increase the Node to correct position by calling HeapIncreaseKey()
        SearchList.get(1).HeapIncreaseKey(nodeIndex, SearchList.get(1).getList().get(nodeIndex)); //O)lgn)

        //After calling HeapIncreaseKey() the order of element might not be correct
        //Reorder by calling HeapSort() (Smallest to Largest)
        SearchList.get(1).HeapSort();   //O(nlgn)
        //Display the top 10 list (Largest to Smallest)
        SearchList.get(1).printQueueReverse();  //O(n)
    }

    /**
     * processKeyword() method traverse the SearchList array list
     * to check if the new keyword has already been search before.
     * @param newKeyword new keyword to be compare with all existed keyword
     * @return  1 = if found the similar keyword (also increment the counter
     * of that keyword)
     *          -1 = if it is unique
     * @time_complexity O(n)
     */
    private int processKeyword(String newKeyword){
        for (int i=1; i<SearchList.size();i++){ //O(n)
            //Format the String with trim and compare with equalsIgnoreCase()
            if (newKeyword.trim().equalsIgnoreCase(SearchList.get(i).getKeyword())) {
                //if the keyword already existed
                SearchList.get(i).incrementCounter();
                return 1;
            }
        }
        return -1;
    }

    /**------------------------------- MaxHeap Functions -------------------------------*/
    /**
     * MaxHeapify() method is used to maintain the max-heap property.
     * The method check to make sure the total score of node at index i is
     * greater than its children's total scores. If it's less than swap the node
     * to correct position
     * @param i = index of current node to be checked
     * @return  none
     * @time_complexity O(lgn)
     */
    private void MaxHeapify(int i){
        int l = Left(i);
        int r = Right(i);
        int max;
        //Compare total score of each node
        if (l<=getHeap_size() && SearchList.get(l).getCounter() > SearchList.get(i).getCounter()) {
            max = l;
        }
        else max = i;
        if (r<=getHeap_size() && SearchList.get(r).getCounter() > SearchList.get(max).getCounter()) {
            max = r;
        }
        if (max != i){
            //Find exchange function (faster)
            swapSearch(i, max);
            MaxHeapify(max);
        }
    }

    /**
     * BuildMaxHeap() method build a Max Heap tree by calling
     * Max-Heapify() on each element in a bottom-up manner.
     * @param none
     * @return  none
     * @time_complexity O(n)
     */
    private void BuildMaxHeap() {
        setHeap_size(SearchList.size());
        for (int i = heap_size/2; i >= 1; i--)
            MaxHeapify(i);
    }

    /**
     * HeapSort() method sort the ArrayList<OneSearch> SearchList
     * by maintaining the as yet unsorted elements as a max-heap.
     * [Call BuildMaxHeap(), move maximum element into correct position,
     * and restore MaxHeap property with MaxHeapigf()]
     * @param none
     * @return  none
     * @time_complexity O(nlgn)
     */
    private void HeapSort() {
        BuildMaxHeap();
        for (int i = getHeap_size(); i>=2; i--){
            swapSearch(1, i);
            setHeap_size(getHeap_size());
            MaxHeapify(1);
        }
        heap_size = SearchList.size()-1; //Size is 1 after heap sort
    }

    /**
     * swapList method is used to swap two OneSearh object in ArrayList
     * @param index1 index of the first object
     * @param index2 index of the second object
     * @return  none
     * @time_complexity O(1)
     */
    private void swapSearch(int index1, int index2){
        OneSearch temp = SearchList.get(index2);
        SearchList.set(index2, SearchList.get(index1));
        SearchList.set(index1, temp);
    }

    /**
     * printKeyword() method print the list of top 10 unique keywords
     * in reverse order (highest to lowest occurrence) after sorting (HeapSort).
     * @param none
     * @return  none
     * @time_complexity O(n)
     */
    private void printKeyword(){
        StringBuffer buff = new StringBuffer();
        buff.append("\n----------- Top 10 unique keyword -----------").append("\n");
        System.out.print(buff);
        int order = 1;
        for (int i = SearchList.size()-1; i>0; i--) //O(n)
        {
            SearchList.get(i).printKeyword(order);
            order++;
        }
    }

    /**---------------- UserInputValidation Functions ----------------*/

    /**
     * UserOptionInputValidation() method makes sure user chooses either
     * option 1 or 2.
     * @param message
     * @param scanner
     * @return  none
     * @time_complexity O(n)
     */
    private int UserOptionInputValidation(String message, Scanner scanner) {
        int num;
        String errorMessage = "Error: Input must be a positive integer (1-2).";
        System.out.print(message);
        while (!scanner.hasNextInt() || (num = scanner.nextInt()) < 0 || num > 3) {
            System.out.print(errorMessage + "\n" + message);
            scanner.nextLine();
        }
        scanner.nextLine();
        return num;
    }

    /**
     * UserNodeInputValidation() method makes sure user chooses SearchResult
     * from 1-10.
     * @param message - instruction message for user
     * @param scanner - scan user input
     * @return  num - valid user input
     * @time_complexity O(n)
     */
    private int UserNodeInputValidation(String message, Scanner scanner) {
        int num;
        String errorMessage = "Error: Input must be a positive integer (1-10).";
        System.out.print(message);
        while (!scanner.hasNextInt() || (num = scanner.nextInt()) <= 0 || num > 10) {
            System.out.print(errorMessage + "\n" + message);
            scanner.nextLine();
        }
        scanner.nextLine();
        return num;
    }

    /**
     * UserFactorInputValidation() method makes sure user chooses Factor scores
     * from 1-4.
     * @param message - instruction message for user
     * @param scanner - scan user input
     * @return  num - valid user input
     * @time_complexity O(n)
     */
    private int UserFactorInputValidation(String message, Scanner scanner) {
        int num;
        String errorMessage = "Error: Input must be a positive integer (1-4).";
        System.out.print(message);
        while (!scanner.hasNextInt() || (num = scanner.nextInt()) <= 0 || num > 4) {
            System.out.print(errorMessage + "\n" + message);
            scanner.nextLine();
        }
        scanner.nextLine();
        return num;
    }

    /**
     * UserScoreInputValidation() method makes sure user chooses a positive new score
     * that is larger than current score and smaller than maximum score.
     * @param message - instruction message for user
     * @param scanner - scan user input
     * @return  num - valid user input
     * @time_complexity O(n)
     */
    private int UserScoreInputValidation(String message, int currentScore, Scanner scanner) {
        int num;
        String errorMessage = "Error: Input must be a positive integer > "
                + currentScore +" (Current Score) and < 25 (Max Score)";
        System.out.print(message);
        while (!scanner.hasNextInt() || (num = scanner.nextInt()) < 0 || num<=currentScore || num > 25) {
            System.out.print(errorMessage + message);
            scanner.nextLine();
        }
        scanner.nextLine();
        return num;
    }
}

