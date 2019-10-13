package comp250_project2;



public class NextWordList {

	private int size;
	//  we can't use this in other class
	private class LinkedListElement {
		// we can't use queue element outside of queue
		// its place here cuz it won't be used outside of queue
		String value; 
		int count;
		LinkedListElement next; // reference to next element in link list

		public LinkedListElement(String value) {
			this.value = value;
			count = 1;
			next = null;
		}

	}

	private LinkedListElement top; // where we pop
	private LinkedListElement bottom; // where we push

	public NextWordList() {
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
		e.count = 1;
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
		// goes through list by traveling through next until it reaches the element at index
		for (int i = 0; i < index; i++) {
			place = place.next;
		}
		// returns element at index
		return place.value;
	}
	
	// returns element at index in list
	private LinkedListElement getElement(int index) {
		// stores top
		LinkedListElement place = top;
		// goes through  list by traveling through next until it reaches element at index
		for (int i = 0; i < index; i++) {
			place = place.next;
		}
		//returns index
		return place;
	}

	
	// changes value at a specific element in list
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
	// will add this nextword to nextword list
	public void foundNextWord(String nextWord) {
		int tryFind = this.find(nextWord); // checks if word is in list
		if (tryFind == -1) { // if it is not in list add it to the nextlist
			this.add(nextWord);
		} else { // if it is already in list increase count
			getElement(tryFind).count++;
		}
	}
	
	public void print() {
		LinkedListElement place = top;
		// print out each nextword, to its own line accompanied by its count
		for (int i = 0 ; i < size; i++) {
			System.out.println("\t" + place.value + " " + place.count);
			place = place.next;
		}
	}
	
	public String getRandomWord() {
		int totalCount = 0;
		LinkedListElement place = top;
		// records number of words in list
		for (int i = 0 ; i < size; i++) {
			totalCount += top.count;
			place = place.next;
		}
		// random choice
		double prechoice = Math.random()*totalCount;
		// truncate this number
		int choice = (int)prechoice;
		
		// since casting to int always trucate, it means that choice will never be
		// equal to total count. the can cause the last nextword to never be reached
		// therefore what must be done is
		// if choice is one less than the size and prechoice is close enough to round it will be
		// increased by one
		if (choice == size - 1 && prechoice - choice >= .50) {
			choice += 1;
		}
		
		
		int runningCount = 0;
		place = top; // keeps track of element
		// it will add the count to the running count until it picks a word
		for (int i = 0; i < size; i++) {
			runningCount += top.count;
			if (runningCount>= choice) { // if runningcount is greater or equal to choice stop searching
				break;
			}
			place = place.next; // move to next element
		}
		return place.value; // returns value at place
	}
}
