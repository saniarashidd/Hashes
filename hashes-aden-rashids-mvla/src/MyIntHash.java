import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class MyIntHash.
 */
public class MyIntHash {
	
	/**
	 * The Enum MODE.
	 */
	enum MODE {Linear, Quadratic,  LinkedList,  Cuckoo};
	
	/** The Constant INITIAL_SIZE. */
	private final static int INITIAL_SIZE = 31;
	
	/** The mode of operation. */
	private MODE mode = MODE.Linear;
	
	/** The physical table size. */
	private int tableSize;
	
	/** The size of the hash - the number of elements in the hash. */
	private int size;
	
	/** The load factor. */
	private double load_factor; 
	
	/** The hash table 1. */
	private int[] hashTable1;
	
	private final static int MAX_QP_OFFSET = 2<<15;
	
	private int max_QP_LOOP;
	
	
	
	
	// The following variables will be defined but not used until later in the project..
	/** The hash table 2. */
	private int[] hashTable2;
	
	/** The hash table LL. */
	private LinkedList<Integer>[] hashTableLL;
	
	
	/**
	 * Instantiates a new my int hash. For Part1 JUnit Testing, the load_factor will be set to 1.0
	 *
	 * @param mode the mode
	 * @param load_factor the load factor
	 */
	public MyIntHash(MODE mode, double load_factor) {
		// TODO Part1: initialize table size, size, mode, and load_factor
		//             Instantiate hashTable1 and initialize it
		tableSize = INITIAL_SIZE;
		size = 0;
		this.mode = mode;
		this.load_factor = load_factor;
		hashTable1 = new int[INITIAL_SIZE];
		initHashTable(hashTable1);
		if((tableSize/2) < MAX_QP_OFFSET) {
			max_QP_LOOP = tableSize/2;
		}
		else {
			max_QP_LOOP = MAX_QP_OFFSET;
		}
		hashTableLL = new LinkedList[INITIAL_SIZE];
		initHashTable(hashTableLL);
		hashTable2 = new int[INITIAL_SIZE];
		initHashTable(hashTable1, hashTable2);
	}
	
	/**
	 * Instantiates a new my int hash.
	 *
	 * @param mode the mode
	 * @param load_factor the load factor
	 * @param testSize the test size
	 */
	public MyIntHash(MODE mode, double load_factor, int initialSize) {
		
		tableSize = initialSize;
		size = 0;
		this.mode = mode;
		this.load_factor = load_factor;
		hashTable1 = new int[initialSize];
		initHashTable(hashTable1);
		if((tableSize/2) < MAX_QP_OFFSET) {
			max_QP_LOOP = tableSize/2;
		}
		else {
			max_QP_LOOP = MAX_QP_OFFSET;
		}
		hashTableLL = new LinkedList[initialSize];
		initHashTable(hashTableLL);
		hashTable2 = new int[initialSize];
		if(mode.equals(mode.Cuckoo)) {
			initHashTable(hashTable1, hashTable2);
		}
		
	}
	
	
	/**
	 * Gets the curr hash load.
	 *
	 * @return the curr hash load
	 */
	public double getCurrHashLoad() {
		//works for LP, QP, LL
		return (size*1.0)/tableSize;
	}
	
	
	/**
	 * Inits the hash LL.
	 *
	 * @param table the table
	 */
	private void initHashTable(LinkedList[] table) {
		
		for(int i = 0; i < tableSize; i++) {
			table[i] = null;
		}
		size = 0;
	}

	
	
	/**
	 * Initializes the provided int[] hashTable - setting all entries to -1
	 * Note that this function will be overloaded to initialize hash tables in other modes
	 * of operation. This method should also reset size to 0!
	 *
	 * @param hashTable the hash table
	 */
	private void initHashTable(int[] hashTable) {
		// TODO Part1: Write this method 
		for(int i = 0; i < hashTable.length; i++) {
			hashTable[i] = -1;
		}
		size = 0;
	}
	
	
	/**
	 * Inits the hash table.
	 *
	 * @param hashTable1 the hash table 1
	 * @param hashTable2 the hash table 2
	 */
	private void initHashTable(int[] hashTable1, int[] hashTable2) {
		for(int i = 0; i < hashTable1.length; i++) {
			hashTable1[i] = -1;
			hashTable2[i] = -1;
		}
		size = 0;
	}
	
	/**
	 * Hash fx.  This is the hash function that translates the key into the index into the hash table.
	 *
	 * @param key the key
	 * @return the int
	 */
	private int hashFx(int key) {
		// TODO Part1: Write this method.
		return key%tableSize;
	}
	
	/**
	 * Hash fx 2.
	 *
	 * @param key the key
	 * @return the int
	 */
	private int hashFx2(int key) {
		return (key/tableSize) % tableSize;
	}
	
	/**
	 * Adds the key to the hash table. Note that this is a helper function that will call the 
	 * required add function based upon the operating mode. However, before calling the specific
	 * add function, determine if the hash should be resized; if so, grow the hash.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean add(int key) {
		
		// TODO: Part2 - if adding this key would cause the the hash load to exceed the load_factor, grow the hash.
		//      Note that you cannot just use size in the numerator... 
		//      Write the code to implement this check and call growHash() if required (no parameters)
		int numTables = 0;
		double loadF;
		
		
		switch (mode) {
			case Linear : 
				numTables = 1;
				loadF = (size+1.0)/(numTables*tableSize);
				if(loadF > load_factor) {
					growHash();
				}
				return add_LP(key);
			case Quadratic : 
				numTables = 1;
				loadF = (size+1.0)/(numTables*tableSize);
				if(loadF > load_factor) {
					growHash();
				}
				return add_QP(key);
			case LinkedList :
				numTables = 1;
				loadF = (size+1.0)/(numTables*tableSize);
				if(loadF > load_factor) {
					growHash();
				}
				return add_LL(key);
			case Cuckoo : 
				numTables = 2;
				loadF = (size+1.0)/(numTables*tableSize);
				if(loadF > load_factor) {
					growHash();
				}
				return add_Cuckoo(key);
			default : return false;
		}
	}
	
	/**
	 * Contains. Note that this is a helper function that will call the 
	 * required contains function based upon the operating mode
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean contains(int key) {
		switch (mode) {
			case Linear : return contains_LP(key); 
			case Quadratic : return contains_QP(key);
			case LinkedList : return contains_LL(key);
			case Cuckoo : return contains_Cuckoo(key);
			default : return false;
		}
	}
	
	/**
	 * Remove. Note that this is a helper function that will call the 
	 * required remove function based upon the operating mode
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean remove(int key) {
		switch (mode) {
			case Linear : return remove_LP(key); 
			case Quadratic : return remove_QP(key);
			case LinkedList : return remove_LL(key);
			case Cuckoo : return remove_Cuckoo(key);
			default : return false;
		}
	}
	
	/**
	 * Grow hash. Note that this is a helper function that will call the 
	 * required overloaded growHash function based upon the operating mode.
	 * It will get the new size of the table, and then grow the Hash. Linear case
	 * is provided as an example....
	 */
	private void growHash() {
		int newSize = getNewTableSize(tableSize);
		int newSizeCuckoo;
		switch (mode) {
		case Linear: growHash(hashTable1,newSize); break;
		case Quadratic : growHash(hashTable1, newSize); break;
		case LinkedList : growHash(hashTableLL, newSize); break;
		case Cuckoo : 
			newSizeCuckoo = getNewTableSizeCuckoo(tableSize);
			growHash(hashTable1, hashTable2, newSizeCuckoo); break;
		}
	}
	
	
	/**
	 * Grow hash.
	 *
	 * @param table the table
	 * @param newSize the new size
	 */
	private void growHash(LinkedList<Integer>[] table, int newSize) {
		LinkedList<Integer>[] savedTable = new LinkedList[tableSize];
		for(int i = 0; i < tableSize; i++) {
			savedTable[i] = table[i];
		}
		
		hashTableLL = new LinkedList[newSize];
		
		tableSize = newSize;
		
		initHashTable(hashTableLL);
		for(int i = 0; i < savedTable.length; i++) {
			if(savedTable[i] != null) {
				for(int j = 0; j < savedTable[i].size(); j++) {
					add(savedTable[i].get(j));
				}
				
			}
			
		}
	}
	
	/**
	 * Grow hash. This the specific function that will grow the hash table in Linear or 
	 * Quadratic modes. This method will:
	 * 	1. save the current hash table, 
	 *  2. create a new version of hashTable1
	 *  3. update tableSize and size
	 *  4. add all valid entries from the old hash table into the new hash table
	 * 
	 * @param table the table
	 * @param newSize the new size
	 */
	private void growHash(int[] table, int newSize) {
		// TODO Part2:  Write this method
	
		int[] savedTable = table.clone(); //found online to make a copy of the table to save
		hashTable1 = new int[newSize];
		tableSize = newSize;
		if((tableSize/2) < MAX_QP_OFFSET) {
			max_QP_LOOP = tableSize/2;
		}
		else {
			max_QP_LOOP = MAX_QP_OFFSET;
		}
		initHashTable(hashTable1);
		for(int i = 0; i < savedTable.length; i++) {
			if(savedTable[i] >= 0) {
				
				add(savedTable[i]);
			}
			
		}
		
	}
	
	/**
	 * Grow hash.
	 *
	 * @param table1 the table 1
	 * @param table2 the table 2
	 * @param newSize the new size
	 */
	private void growHash(int[] table1, int[] table2, int newSize) {
		int[] savedTable1 = table1.clone();
		int[] savedTable2 = table2.clone();
		
		
		hashTable1 = new int[newSize];
		hashTable2 = new int[newSize];
		
		
		tableSize = newSize;
		initHashTable(hashTable1, hashTable2);
		
		for(int i = 0; i < savedTable1.length; i++) {
			if(savedTable1[i] >= 0) {
				add(savedTable1[i]);
			}
			if(savedTable2[i] >= 0) {
				add(savedTable2[i]);
			}
		}
		
	}
	
	
	/**
	 * Gets the new table size. Finds the next prime number
	 * that is greater than 2x the passed in size (startSize)
	 *
	 * @param startSize the start size
	 * @return the new table size
	 */
	private int getNewTableSize(int startSize) {
		// TODO Part2: Write this method
		int newSize = 2*startSize+1;
		while(!isPrime(newSize)) {
			newSize++;
		}
		return newSize;
	}
	
	/**
	 * Gets the new table size cuckoo.
	 *
	 * @param startSize the start size
	 * @return the new table size cuckoo
	 */
	private int getNewTableSizeCuckoo(int startSize) {
		int newSize = startSize+1000;
		while(!isPrime(newSize)) {
			newSize++;
		}
		return newSize;
	}
	
	/**
	 * Checks if is prime.  
	 *
	 * @param size the size
	 * @return true, if is prime
	 */
	private boolean isPrime(int size) {
		// TODO Part2: Write this method
		int sqrt = (int)Math.ceil((Math.sqrt(size)));
		for(int i = 2; i < sqrt; i++) {
			if(size%i == 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Adds the key using the Linear probing strategy:
	 * 
	 * 1) Find the first empty slot sequentially, starting at the index from hashFx(key)
	 * 2) Update the hash table with the key
	 * 3) increment the size
	 * 
	 * If no empty slots are found, return false - this would indicate that the hash needs to grow...
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean add_LP(int key) {
		// TODO Part1: Write this function
		int index = hashFx(key);

		for(int i = 0; i < tableSize; i++) {
			if(hashTable1[index] == key) {
				return false;
			}
			else if(hashTable1[index] < 0) {
				hashTable1[index] = key;
				size++;
				return true;
			}
			index++;
			if(index == tableSize) {
				index = 0;
			}
		}
		
		return false;
	}
	

	
	/**
	 * Contains - uses the Linear Probing method to determine if the key exists in the hash
	 * A key condition is that there are no open spaces between any values with collisions, 
	 * independent of where they are stored.
	 * 
	 * Starting a the index from hashFx(key), sequentially search through the hash until:
	 * a) the key matches the value at the index --> return true
	 * b) there is no valid data at the current index --> return false
	 * 
	 * If no matches found after walking through the entire table, return false
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean contains_LP(int key) {
		// TODO Part1: Write this method.
		int index = hashFx(key);
		for(int i = 0; i < tableSize; i++) {
			if(hashTable1[index] == key) {
				return true;
			}
			if(hashTable1[index] < 0) {
				return false;
			}
			index++;
			if(index == tableSize) {
				index = 0;
			}
		}
		return false;
		
	}
	
	/**
	 * Remove - uses the Linear Problem method to evict a key from the hash, if it exists
	 * A key requirement of this function is that the evicted key cannot introduce an open space
	 * if there are subsequent values which had collisions...
	 * 
	 * 1) Identify if the key exists by walking sequentially through the hash table, starting at hashFx(key) 
	 *    - if not return false,
	 * 2) from the index where the key value was found, search sequentially through the table, recording
	 *    any values that collide with hashFx(key) until either an open space if found or the full table has been processed.
	 *    If a collision was found, replace the key value with the collision value, and set the value at the collision index to an open space;
	 *    otherwise, set the key value to indicate an open space... I would recommend writing a helper method to implement this logic; it
	 *    would simply return the value to replace the key value with...
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean remove_LP(int key) {
		// TODO Part2: Write this function
		int index = hashFx(key);
		boolean found = false;
		for(int i = 0; i < tableSize; i++) {
			if(hashTable1[index] == key) {
				found = true;
				break;
			}
			index++;
			if(index == tableSize) {
				index = 0;
			}
		}
		if(found) {
			if(index+1 < tableSize && hashTable1[index+1] == -1) {
				hashTable1[index] = -1;
			}
			else if(index+1 == tableSize) {
				if(hashTable1[0] == -1) {
					hashTable1[index] = -1;
				}
				else {
					hashTable1[index] = -2;
				}
			}
			else {
				hashTable1[index] = -2;
			}
			size--;
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * Adds the QP.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean add_QP(int key) {
		int bIndex = hashFx(key);
		int index;
	
		while(true) {
			for(int i = 0; i < max_QP_LOOP; i++) {
				index = (bIndex + (i*i))%tableSize;
				if(hashTable1[index] == key) {
					return false;
				}
				else if(hashTable1[index] < 0) {
					hashTable1[index] = key;
					size++;
					return true;
				}
				else if(index == tableSize) {
					break;
					
					
				}
				
			}
			growHash();
			bIndex = hashFx(key);
			
			
		}
		
		
		
		
	}
	
	/**
	 * Contains QP.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean contains_QP(int key) {
		int bIndex = hashFx(key);
		int index;
		for(int i = 0; i < max_QP_LOOP; i++) {
			index = (bIndex + (i*i))%tableSize;
			if(hashTable1[index] == key) {
				return true;
			}
			if(hashTable1[index] < 0) {
				return false;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Removes the QP.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean remove_QP(int key) {
		
		int bIndex = hashFx(key);
		int index = 0;
		boolean found = false;
		for(int i = 0; i < max_QP_LOOP; i++) {
			index = (bIndex+ i*i)%tableSize;
			if(hashTable1[index] == key) {
				found = true;
				break;
			}
			
			
		}
		if(found) {
			hashTable1[index] = -2;
			size--;
			return true;
		}
		return false;
	
	}
	
	
	/**
	 * Adds the LL.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean add_LL(Integer key) {
		int index = hashFx(key);
		if(hashTableLL[index] == null) {
			hashTableLL[index] = new LinkedList<Integer>();
			hashTableLL[index].add(key);
			size++;
			return true;
		}
		else if(hashTableLL[index].contains(key)) {
			return false;
		}
		else {
			hashTableLL[index].add(key);
			size++;
			return true;
		}
		
	}
	
	
	/**
	 * Contains LL.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean contains_LL(Integer key) {
		int index = hashFx(key);
		if(hashTableLL[index] == null) {
			return false;
		}
		
		return hashTableLL[index].contains(key);
	}
	
	
	/**
	 * Removes the LL.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean remove_LL(Integer key) {
		int index = hashFx(key);
		
		if(hashTableLL[index] == null) {
			return false;
		}
		boolean removed = hashTableLL[index].remove(key);
		if(removed) {
			if(hashTableLL[index].isEmpty()) {
				hashTableLL[index] = null;
			}
			size--;
		}
		return removed;
	}
	
	
	/**
	 * Adds the cuckoo.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean add_Cuckoo(int key) {
		if(contains_Cuckoo(key)) {
			return false;
		}
		while(!placeCuckoo(key, true, key)) { 
			growHash();
		}
		
		size++;
		return true;
		
		
	}
	
	/**
	 * Place cuckoo.
	 *
	 * @return true, if successful
	 */
	public boolean placeCuckoo(int key, boolean isPTable, int val) {
		int index;
		int evictedKey;
		int valAtIndex;
		if(isPTable) {
			index = hashFx(key);
			valAtIndex = hashTable1[index];
			hashTable1[index] = key;
			if(valAtIndex >= 0) {
				evictedKey = valAtIndex;
				return placeCuckoo(evictedKey, false, val);
			}
			else {
				return true;
			}
		}
		else {
			index = hashFx2(key);
			valAtIndex = hashTable2[index];
			hashTable2[index] = key;
			if(valAtIndex >= 0) {
				evictedKey = valAtIndex;
				if(evictedKey == val) {
					return false;
				}
				return placeCuckoo(evictedKey, true, val);
			}
			else {
				return true;
			}	
		}
		
	}
	
	/**
	 * Contains cuckoo.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean contains_Cuckoo(int key) {
		int index = hashFx(key);
		int index2 = hashFx2(key);
		if(hashTable1[index] == key || hashTable2[index2] == key) {
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the cuckoo.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean remove_Cuckoo(int key) {
		int index = hashFx(key);
		int index2 = hashFx2(key);
		if(hashTable1[index] == key) { 
			hashTable1[index] = -1;
			return true;
		}
		else if(hashTable2[index2] == key) {
			hashTable2[index2] = -1;
			return true;
		}
		return false;
	}
	
		
	/**
	 * Gets the hash at. Returns the value of the hash at the specified index, and (if required by the operating mode) the specified offset.
	 * Use a switch statement to implement this code. This is FOR DEBUG AND TESTING PURPOSES ONLY
	 * 
	 * @param index the index
	 * @param offset the offset
	 * @return the hash at
	 */
	Integer getHashAt(int index, int offset) {
		// TODO Part1: as you code this project, you will add different cases. for now, complete the case for Linear Probing
		switch (mode) {
		 case Linear : 
			 return hashTable1[index];
		 case Quadratic :
			 return hashTable1[index];
		 case LinkedList : 
			 if(hashTableLL[index] == null) {
				 return null;
			 }
			 else if(offset < 0 || offset >= hashTableLL[index].size()) {
				 return -1;
			 }
			 else {
				 return hashTableLL[index].get(offset);
			 }
		 case Cuckoo :
			 if(offset == 0) {
				 return hashTable1[index];
			 }
			 if(offset == 1) {
				 return hashTable2[index];
			 }
		}
		return -1;
	}
	
	/**
	 * Gets the number of elements in the Hash
	 *
	 * @return size
	 */
	public int size() {
		// TODO Part1: Write this method
		return size;
	}

	/**
	 * resets all entries of the hash to -1. This should reuse existing code!!
	 *
	 */
	public void clear() {
		// TODO Part1: Write this method
		switch (mode) {
			case Linear : initHashTable(hashTable1); break;
			case Quadratic : initHashTable(hashTable1); break;
			case LinkedList : initHashTable(hashTableLL); break;
			case Cuckoo : initHashTable(hashTable1, hashTable2); break;
 		}
	}

	/**
	 * Returns a boolean to indicate of the hash is empty
	 *
	 * @return ????
	 */
	public boolean isEmpty() {
		// TODO Part1: Write this method
		
		int cnt = 0;
		switch (mode) {
		case Linear : 
		case Quadratic :
			for(int i = 0; i < size; i++) {
				if(hashTable1[i] != -1) {
					cnt++;
				}
			}
			return cnt==0;
			
		case LinkedList :
			for(int i = 0; i < size; i++) {
				if(hashTableLL[i] != null) {
					cnt++;
				}
			}
			return cnt==0;
		case Cuckoo :
			for(int i = 0; i < tableSize; i++) {
				if(hashTable1[i] != -1) {
					cnt++;
				}
				if(hashTable2[i] != -1) {
					cnt++;
				}
			}
			return cnt==0;
		default : return false;
		}
		
	}

	/**
	 * Gets the load factor.
	 *
	 * @return the load factor
	 */
	public double getLoad_factor() {
		return load_factor;
	}

	/**
	 * Sets the load factor.
	 *
	 * @param load_factor the new load factor
	 */
	public void setLoad_factor(double load_factor) {
		this.load_factor = load_factor;
	}

	/**
	 * Gets the table size.
	 *
	 * @return the table size
	 */
	public int getTableSize() {
		return tableSize;
	}

}
