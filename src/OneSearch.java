import java.util.ArrayList;
/**
 * OneSearch class
 *  - stores the data of one search (search keyword, list of websites
 * counter (number of occurrence of keyword), and heap_size)
 *  - has constructor, setters and getters to access the private variables,
 * and print the value of the object.
 *  -  have heap, max heap, and max priority queue function to build
 * a priority queue of top 10 websites.
 */

public class OneSearch {
    public String keyword;
    public ArrayList<SearchResult> list = new ArrayList<SearchResult>();
    public int counter;
    public int heap_size;

    /** Default Constructor */
    public OneSearch(){ }

    /** Setters */
    public void setCounter(int counter) { this.counter = counter; }

    public void setHeap_size(int heap_size) { this.heap_size = heap_size -1; }

    public void setKeyword(String keyword) { this.keyword = keyword; }

    public void setList(ArrayList<SearchResult> list) { this.list = list; }

    /** Getters */
    public String getKeyword() { return keyword; }

    public ArrayList<SearchResult> getList() { return list; }

    private int getHeap_size() { return heap_size; }

    public int getCounter() { return counter; }

    public void incrementCounter(){ counter++; }

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
    public void MaxHeapify(int i){
        int l = Left(i);
        int r = Right(i);
        int max;
        //Compare total score of each node
        if (l<=getHeap_size() && list.get(l).getTotalScore() > list.get(i).getTotalScore()) {
            max = l;
        }
        else max = i;
        if (r<=getHeap_size() && list.get(r).getTotalScore() > list.get(max).getTotalScore()) {
            max = r;
        }
        if (max != i){
            //Find exchange function (faster)
            swapList(i, max);
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
    public void BuildMaxHeap() {
        setHeap_size(list.size());
        for (int i = heap_size/2; i >= 1; i--) //O(n)
            MaxHeapify(i); //O(lgn)
    }

    /**
     * HeapSort() method sort the ArrayList<SearchResult> list
     * by maintaining the as yet unsorted elements as a max-heap.
     * [Call BuildMaxHeap(), move maximum element into correct position,
     * and restore MaxHeap property with MaxHeapigf()]
     * @param none
     * @return  none
     * @time_complexity O(nlgn)
     */
    public void HeapSort() {
        BuildMaxHeap(); //O(n)
        for (int i = getHeap_size(); i>=2; i--){
            swapList(1, i);
            setHeap_size(getHeap_size());
            MaxHeapify(1); //O(lgn)
        }
        heap_size = list.size()-1; //Size is 1 after heap sort
    }

    /**------------------------------- Max Priority Queue Functions -------------------------------*/

    /**
     * MaxHeapInsert() method inserts SearchResult key into the ArrayList list.
     * @param key SearchResult object to be inserted to the max heap
     * @return  none
     * @time_complexity O(lgn)
     */
    public void MaxHeapInsert(SearchResult key) throws Exception {
        setHeap_size(list.size()+1);
        SearchResult temp = new SearchResult();
        temp.setTotalScore(-1000);
        list.set(heap_size, temp);
        HeapIncreaseKey(heap_size, key); //O(lgn)
    }

    /**
     * HeapMaximum() method returns SearchResult object with the largest total score.
     * @param none
     * @return  returns SearchResult object with the largest total score
     * @time_complexity O(1)
     */
    public SearchResult HeapMaximum(){
        return list.get(1);
    }

    /**
     * HeapExtractMax() method remove and returns SearchResult object with
     * the largest total score.
     * @param none
     * @return  max = SearchResult object with the largest total score
     * @exception Exception on Heap Underflow
     * @time_complexity O(lgn)
     */
    public SearchResult HeapExtractMax() throws Exception {
        if (getHeap_size() < 1)
            throw new Exception("Heap Underflow");
        SearchResult max = list.get(1);
        list.set(1, list.get(getHeap_size()));
        setHeap_size(getHeap_size());
        MaxHeapify(1);
        return max;
    }

    /**
     * HeapIncreaseKey() method increases the SearchResult at index x
     * to new SearchResult key
     * @param index index of the current SearchResult object to be updated
     * @param key   same SearchResult object with higher total score
     * @return  none
     * @exception Exception if the new total score is smaller than current one
     * @time_complexity O(lgn)
     */
    public void HeapIncreaseKey(int index, SearchResult key) throws Exception {
        if (key.getTotalScore() < list.get(index).getTotalScore())
            throw new Exception("New key is smaller than current key");
        list.set(index, key);
        while (index>1 && list.get(Parent(index)).getTotalScore() < list.get(index).getTotalScore()){
            swapList(index, Parent(index));
            index = Parent(index);
        }
    }
    /**
     * PriorityQueue() method extract 10 highest node from ArrayList list,
     * add to temporary Arraylist and assign them back to list to create
     * priority queue of 10 node
     * @param none
     * @return  none
     * @exception Exception on Heap Underflow
     * @time_complexity O(nlgn)
     */
    public void PriorityQueue() throws Exception {
        ArrayList<SearchResult> queue = new ArrayList<SearchResult>();
        int index = 0;
        //Node at index 0 is null
        queue.add(index, null); //O(1)
        //Extract 10 largest node and add to queue
        for (index=1; index<=10; index++) { //O(n)
            queue.add(index, HeapExtractMax()); //O(lgn)
        }
        list = queue;   //Assign queue back to list (reuse list)
    }

    /**
     * swapList method is used to swap two SearchResult object in ArrayList
     * @param index1 index of the first object
     * @param index2 index of the second object
     * @return  none
     * @time_complexity O(1)
     */
    public void swapList(int index1, int index2){
        SearchResult temp = list.get(index2); //O(1)
        list.set(index2, list.get(index1)); //O(1)
        list.set(index1, temp);          //O(1)
    }

    /**
     * findNode method find SearchResult node in the array list
     * that has the same url
     * @param url (String) used to compare to find exact match
     * @return  i = index of the node in arrayList if found
     * @return  -1 if not found
     * @time_complexity O(n)
     */
    public int findNode(String url){
        for (int i = 1; i < list.size(); i++) { //O(n)
            if (url.equals(list.get(i).getUrl())) //O(1)
                return i;
        }
        return -1;
    }
    /**
     * printList() method is used StringBuffer to display one object
     * OneSearch (Keyword) and SearchResult object by calling SearchResult's
     * printAll function in Reverse order
     * @param none
     * @return none
     * @time_complexity O(n^2)
     */
    public void printList(){
        StringBuffer buff = new StringBuffer();
        buff.append("KEYWORD: ").append(keyword).append("\n\n");
        System.out.print(buff);
        for (int i = 1; i < list.size(); i++)
            list.get(i).printAll(i); //O(n)
    }

    /**
     * printQueueReverse() method is used StringBuffer to display one object
     * OneSearch (Keyword) and SearchResult object by calling SearchResult's
     * printAll function in Reverse order
     * @param none
     * @return none
     * @time_complexity O(n^2)
     */
    public void printQueueReverse(){
        StringBuffer buff = new StringBuffer();
        buff.append("KEYWORD: ").append(keyword).append("\n\n");
        System.out.print(buff);
        for (int i = list.size()-1; i>0; i--) //O(n)
            list.get(i).printAll(11-i); //O(n)
    }
    /**
     * printKeyword() method is used StringBuffer to display keyword
     * and the number of occurrence the keyword has been searched
     * @param rank used to display the position of keyword on the list
     * @return none
     * @time_complexity O(1)
     */
    public void printKeyword(int rank){
        StringBuffer buff = new StringBuffer();
        buff.append("\n").append(rank).append(". ").append("Keyword: ").append(keyword);
        buff.append(" [").append(counter).append(" time(s)]");
        System.out.print(buff);
    }
}