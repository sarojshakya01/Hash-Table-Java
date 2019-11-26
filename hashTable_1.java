// Java code illustrating clear() and clone() methods 
import java.io.*;
import java.util.*;
import java.lang.*;

@SuppressWarnings("unchecked")
class hashTable_1 <Key, Value> {

	private static final int TABLE_SIZE = 128;

	private int n_elem;		// number of keys in the hash table
	private int size;		// size of linear hash table
	private Key[] key;		// key
	private int[] n_probe;
	private int[] hash;
	private Value[] value;

	public hashTable_1() {
		this(TABLE_SIZE);
	}

	public hashTable_1(int size) {
		n_elem = 0;
		this.size = size;
		key = (Key[]) new Object[size];
		n_probe = new int[size];
		hash = new int[size];
		value = (Value[]) new Object[size];
	}

	public int getSize() {
		return n_elem;
	}

	public int hash(Key k) {

		//HA = {abs[ slice(4..5) ] + abs[slice(13..14) ] } / 65,535 + abs[slice(10..10)] 

		int hkey = 0;
		
		String sKey = (String)k;
		int int4 = sKey.charAt(3);
		int int5 = sKey.charAt(4);
		String char45 = Integer.toString(int4) + Integer.toString(int5);
		int int45 = Integer.parseInt(char45);

		int int13 = sKey.charAt(12);
		int int14 = sKey.charAt(13);
		String char1314 = Integer.toString(int13) + Integer.toString(int14);
		int int1314 = Integer.parseInt(char1314);

		int int10 = sKey.charAt(9);
		
		hkey = (Math.abs(int45) + Math.abs(int1314))/65535 + Math.abs(int10);
		return hkey;
	}

	private void resize(int size) {
		hashTable_1<Key, Value> temp = new hashTable_1<Key, Value>(size);
		for (int i = 0; i < size; i++) {
			if (key[i] != null) {
				temp.putValue(key[i], value[i]);
			}
		}
		key = temp.key;
		value = temp.value;
		hash = temp.hash;
		n_probe = temp.n_probe;
		size = temp.size;
	}

	public void deleteKey(Key k) {
		if (getValue(k) == null) return;

		// find position ith of key
		int i = hash(k);
		while (!k.equals(key[i])) {
			i = (i + 1) % size;
		}

		// delete key - value
		key[i] = null;
		value[i] = null;
		hash[i] = -1;
		n_probe[i] = 0;

		// Re-hash all key
		i = (i + 1) % size;
		while (key[i] != null) {
			// delete key[i] an value[i] and reinsert
			Key reHashKey = key[i];
			Value reHashVal = value[i];
			key[i] = null;
			value[i] = null;
			hash[i] = -1;
			n_probe[i] = 0;
			n_elem--;
			putValue(reHashKey, reHashVal);
			i = (i + 1) % size;
		}

		n_elem--;

		// halves size if less or equal to 12.5%
		if (n_elem > 0 && n_elem <= size/8) resize(size/2);

	}

	public void putValue(Key k, Value val) {
		if (val == null) {
			deleteKey(k);
			return;
		}
		
		// double table size if 50% full
		if (n_elem >= size/2) resize(2*size);
		int i;
		int countProbes = 0;
		for (i = hash(k); key[i] != null; i = (i + 1) % size) {
			System.out.println(k);
			countProbes++; 
			if (key[i].equals(k)) {
				System.out.println("here");
				value[i] = val;
				hash[i] = i;
				return;
			}
		}
		key[i] = k;
		value[i] = val;
		hash[i] = i;
		n_elem++;
	}

	public Value getValue(Key k) {
		for (int i = hash(k); key[i] != null; i = (i + 1) % size)
			if (key[i].equals(k))
				return value[i];
		return null;
	}

	public int getHash(Key k) {
		if (k == null) throw new IllegalArgumentException("argument to get() is null");
		for (int i = hash(k); key[i] != null; i = (i + 1) % size)
			if (key[i].equals(k))
				return hash[i];
		return -1;
	}

	public Iterable<Key> keyList() {
		Queue<Key> queue = new LinkedList<Key>();
		for (int i = 0; i < size; i++)
			if (key[i] != null) queue.add(key[i]);
		return queue;
	}

	public static void main(String[] arg) throws FileNotFoundException {
		File file = new File("./words.txt");
		Scanner sc = new Scanner(file);
		List <String> list = new ArrayList <String>();
		
		while (sc.hasNextLine()) {
			list.add(sc.nextLine());
			// System.out.println(sc.nextLine());
		}

		hashTable_1 <String, Integer> h = new hashTable_1<String, Integer>();
		
		String key = "";

		for (int i = 0; i < 5 /*list.size()*/; i++) {
			key = list.get(i);
			h.putValue(key, i+1);
			//h1.putValue(key, i);
		}

		for (String s: h.keyList()){
			System.out.println(s + " " + Integer.toString(h.getHash(s)));	
		}

	} 
} 
