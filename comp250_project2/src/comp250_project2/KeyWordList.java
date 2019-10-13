package comp250_project2;


public class KeyWordList {

	private int size;
	//  we can't use this in other class
	private class LinkedListElement {
		// we can't use queue element outside of queue
		// its place here cuz it won't be used outside of queue
		String value; 
		LinkedListElement next; // reference to next element in link list
		NextWordList nextList;
		
		public LinkedListElement(String value) {
			this.value = value;
			next = null;
		}
	}

	private LinkedListElement top; // where we pop
	private LinkedListElement bottom; // where we push

	public KeyWordList() {
		// it starts by assigning null to bottom, then when we do this it passes the null value to top. so they 
		// both get assign null
		top = bottom = null;
		size = 0; // size of array 
	}

	public boolean isEmpty() { // if top and bottom is empty then  the link list is empty
		return top == null && bottom == null;
	}

	// add element to list
	public void add(String data) {
		// make new element
		LinkedListElement e = new LinkedListElement(data);
		e.nextList = new NextWordList();
		if(bottom == null) {
			// if the bottom is null then it will jusy make top and bottom this element because the list
			// was empty 
			top = e;
			bottom = e;
		} else {
			// make the bottom next object, equal to this object, move bottom to this object
			bottom.next = e;
			bottom = e;
		}
		// increment size
		size++;
	}

	public String pop() {
		// stores the top
		LinkedListElement e=top;
		top = e.next; // this could make pop null
		if (top == null) {
			// if top is null then it will make the bottom null
			// why? because the first element we made has 2 null references,
			// so we could get our top to be null by popping so much, so if top is null
			// we make bottom null
			bottom = null;
		}
		size--; /// decrement size
		return e.value;
	}

	
	public String get(int index) {
		// stores top
		LinkedListElement place = top;
		// goes through  list by traveling through next
		for (int i = 0; i < index; i++) {
			place = place.next;
		}
		return place.value;
	}
	
	// returns the linked list element
	private LinkedListElement getElement(int index) {
		// stores top
		LinkedListElement place = top;
		// goes through  list by traveling through next
		for (int i = 0; i < index; i++) {
			place = place.next;
		}
		return place;
	}

	
	public void set(String newvalue, int index) {
		LinkedListElement place = top;

		// goes through list by traveling through next, will stop at the right index
		for (int i = 0; i < index; i++) {
			place = place.next;
		}
		// changes place value to new value
		place.value = newvalue;

	}

	public void remove(int index) {
		LinkedListElement place = top;
		// if the index is 0, it will move top because the link list isn't made to travel backwards
		if (index == 0) {
			top = top.next;
		} else {
			for (int i = 0; i < index-1; i++) {
				place = place.next;
			}
			// removes it from list by remvoing the reference to that item
			// so it takes this elements next reference and connects to the element after the one which 
			// is suppose to be deleted
			place.next = place.next.next;
			
		}
		// if the index is last item, it will change bottom to the 2rd to last item
		if(index == size - 1) {
			bottom = place;
		}
		// increase size
		size--;
		// if top ends up being null make bottom null
		if (top == null) {
			bottom = null;
		}
	}
	
	public int find(String thingtoFind) {
		LinkedListElement place= top; 
		// go through entire linkedlist
		for (int i = 0 ; i<size; i++) {
			// if it equals this value return index
			if (place.value.equals(thingtoFind)) {
				return i;
			}
			place = place.next;
		}
		// if it doesn't match any of the values it returns -1
		return -1;
	}

	public int getSize() {
		return size;
	}
	
	public void print() {
		LinkedListElement place = top; // placeholder
		// loop for size of list
		for (int i = 0; i < size; i++) {
			String word = place.value;
			// will print out the keyword
			System.out.println(word + ":");
			// print out keyword nextlist after
			place.nextList.print();
			System.out.println();
			place = place.next; // move to next element
		}
	}
	
	// adds new object if it does not exist in the list yet, also returns the location of the element, either input or newly created
	public int addUnique(String s) {
		int findIndicator = this.find(s); // keeps track of where this element is
		if (findIndicator == -1) {
			// if its not in the list, we will add it, then change the location to the last item in list
			this.add(s);
			findIndicator = size - 1;
		}
		return findIndicator;
	}

	public void foundWordSequence(String keyword, String nextWord) {
		int elementIndex = addUnique(keyword); // will add this keyword if its not yet in the list
		this.getElement(elementIndex).nextList.foundNextWord(nextWord);; // will add an entry to its nextlist if needed if not increase count
	}
	
	// this finds the keyword, then gets the element and gets a return word
	public String getRandomNextWord(String keyword) {
		return getElement(this.find(keyword)).nextList.getRandomWord();
	}
	
	// this gets a random keyword
	public String getRandomKeyword() {
		int choice = (int)(Math.random()*size);
		return get(choice);
	}
	
	// this gets the value in the bottom element in list
	public String getBottomValue() {
		return bottom.value;
	}


}
