import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@SuppressWarnings("unchecked")
class hashTable {

	private static final int TABLE_SIZE = 128;
	private int [] arrForRandomNum;
	private boolean firstCall = true;

	private int n_key;		// number of keys in the hash table
	private int size;		// size of linear hash table
	private String[] key;	// key
	private int[] n_probe;	// number of probes
	private int[] i_hash;	// hash (index)
	private int[] hash;		// hash (index)
	private int[] value;	// value

	public hashTable() {
		this(TABLE_SIZE);
	}

	public hashTable(int size) {
		n_key = 0;
		this.size = size;
		key = new String[size];
		n_probe = new int[size];
		i_hash = new int[size];
		hash = new int[size];
		value = new int[size];
	}

	public int hash(String k) {

		//HA = {abs[ slice(4..5) ] + abs[slice(13..14) ] } / 65,535 + abs[slice(10..10)] 

		int hkey = 0;
		String char45 = k.substring(3,5);
		String char1314 = k.substring(11,13);
		String char10 = k.substring(9,10);;

		hkey = (Math.abs(char45.hashCode()) + Math.abs(char1314.hashCode()))/65535 + Math.abs(char10.hashCode());
		/*
		// without using hashCode function
		int hkey = 0;
		
		int int4 = k.charAt(3);
		int int5 = k.charAt(4);
		String char45 = Integer.toString(int4) + Integer.toString(int5);
		int int45 = Integer.parseInt(char45);

		int int13 = k.charAt(12);
		int int14 = k.charAt(13);
		String char1314 = Integer.toString(int13) + Integer.toString(int14);
		int int1314 = Integer.parseInt(char1314);

		int int10 = k.charAt(9);

		hkey = (Math.abs(int45) + Math.abs(int1314))/65535 + Math.abs(int10);
		*/
		return hkey;
	}

	private void resize(int s) {
		hashTable temp = new hashTable(s);
		for (int i = 0; i < size; i++) {
			if (key[i] != null) {
				temp.putValue(key[i], value[i]);
			}
		}
		key = temp.key;
		value = temp.value;
		i_hash = temp.i_hash;
		hash = temp.hash;
		n_probe = temp.n_probe;
		size = temp.size;
	}

	private void resetTable () {
		hashTable temp = new hashTable(size);
		key = temp.key;
		value = temp.value;
		i_hash = temp.i_hash;
		hash = temp.hash;
		n_probe = temp.n_probe;
		size = temp.size;
		n_key = temp.n_key;
		arrForRandomNum = temp.arrForRandomNum;
		firstCall = temp.firstCall;
	}

	public void putValue(String k, int val) {

		// double table size if 50% full
		// if (n_key >= size/2) {
		// 	resize(2*size);
		// }

		int i;
		int countProbes = 0;

		for (i = hash(k); key[i] != null; i = (i + 1) % size) {
			countProbes++;
			if (key[i].equals(k)) {
				value[i] = val;
				hash[i] = i;
				return;
			}
		}
		key[i] = k;
		value[i] = val;
		i_hash[i] = hash(k);
		hash[i] = i;
		n_probe[i] = countProbes;
		n_key++;
	}

	private int getNumber(int [] arr) {
		int n = arr.length;
		int index = (int)(Math.random() * n);
		int num = arr[index];
		return num;
	}

	private void removeUsedIndex(int n, int [] arr) {
		arrForRandomNum = new int[arr.length-1];
		int count = 0;
		for (int i = 0; i < arr.length; i++) {
			if (n != arr[i]) {
				arrForRandomNum[count] = arr[i];
				count++;
			}
		}
	}

	private int genRandomNum(int n) {
		int a = getNumber(arrForRandomNum);
		return a;
	}

	private void fillList (int n) {
		arrForRandomNum = new int[n];
		for (int i = 0; i < n; i++) {
			arrForRandomNum[i] = i;
		}
	}

	public void putValueRandom(String k, int val) {
		
		// double table size if 50% full
		// if (n_key >= size/2) {
		// 	resize(2*size);
		// }

		int i;
		int countProbes = 0;
		
		for (i = hash(k); key[i] != null; i = (i + genRandomNum(size - 1)) % size) {
			countProbes++;
			if (key[i].equals(k)) {
				value[i] = val;
				hash[i] = i;
				return;
			}
		}
		removeUsedIndex(i, arrForRandomNum);
		key[i] = k;
		value[i] = val;
		i_hash[i] = hash(k);
		hash[i] = i;
		n_probe[i] = countProbes;
		n_key++;
	}

	public int getSize() {
		return n_key;
	}

	public int getCapacity() {
		return size;
	}

	public int getValue(String k) {
		for (int i = hash(k); key[i] != null; i = (i + 1) % size)
			if (key[i].equals(k))
				return value[i];
		return -1;
	}

	public int getValueRandom(String k) {
		for (int i = 0; i < size; i++)
			if (key[i] != null && key[i].equals(k))
				return value[i];
		return -1;
	}

	public int getHash(String k) {
		for (int i = hash(k); key[i] != null; i = (i + 1) % size)
			if (key[i].equals(k))
				return hash[i];
		return -1;
	}

	public int getHashRandom(String k) {
		for (int i = 0; i < size; i++)
			if (key[i] != null && key[i].equals(k))
				return hash[i];
		return -1;
	}

	public int getiHash(String k) {
		for (int i = hash(k); key[i] != null; i = (i + 1) % size)
			if (key[i].equals(k))
				return i_hash[i];
		return -1;
	}

	public int getiHashRandom(String k) {
		for (int i = 0; i < size; i++)
			if (key[i] != null && key[i].equals(k))
				return i_hash[i];
		return -1;
	}

	public int getProbe(String k) {
		for (int i = hash(k); key[i] != null; i = (i + 1) % size)
			if (key[i].equals(k))
				return n_probe[i];
		return -1;
	}

	public int getProbeRandom(String k) {
		for (int i = 0; i < size; i++)
			if (key[i] != null && key[i].equals(k))
				return n_probe[i];
		return -1;
	}

	public String [] getKeyList() {
		String [] list = new String[n_key];
		int count = 0;
		for (int i = 0; i < size; i++) {
			if (key[i] != null) {
				list[count] = key[i];
				count++;
			}
		}

		return list;
	}

	public static void main(String[] arg) throws FileNotFoundException {
		File file = new File("./words.txt");
		Scanner sc = new Scanner(file);
		String [] list = new String[500];
		int i = 0, tot_key;

		while (sc.hasNextLine()) {
			list[i] = sc.nextLine();
			i++;
		}
		tot_key = i;

		String key = "";
		
		hashTable h = new hashTable(128); // Create Hash table with capacity 128

		int minProbe = h.getCapacity();;
		int maxProbe = 0;
		int sumProbe = 0;

		// OPTION C(A)
		System.out.println("\nSolution of OPTION C(A)\n");
		for (i = 0; i < h.getCapacity()*0.5; i++) {
			key = list[i];
			h.putValue(key, i+1);
		}

		i = 0; // reset the var

		for (String s: h.getKeyList()){
			i++;
			if (h.getProbe(s) < minProbe) {
				minProbe = h.getProbe(s);
			}
			if (h.getProbe(s) > maxProbe) {
				maxProbe = h.getProbe(s);
			}
			sumProbe = sumProbe + h.getProbe(s);
			if (i == 30) break;
		}

		System.out.println("Minimum Probe: " + minProbe);
		System.out.println("Maximum Probe: " + maxProbe);
		System.out.println("Average Probe: " + String.format("%.3g%n", (float)sumProbe/30));
		
		System.out.println(String.format("%-6s","S.N.") + String.format("%-17s","Key") + String.format("%-14s", "Init. Hash") + String.format("%-14s", "Hash/Index") + String.format("%-3s","Probe"));
		for (String s: h.getKeyList()){
			System.out.println(String.format("%-6d",h.getValue(s)) + s + " " + String.format("%-14d",h.getiHash(s)) + String.format("%-14d",h.getHash(s)) + h.getProbe(s));
		}

		// OPTION C(B)
		h.resetTable();
		System.out.println("\nSolution of OPTION C(B)\n");
		for (i = 0; i < h.getCapacity()*0.9; i++) {
			key = list[i];
			h.putValue(key, i+1);
		}

		// reset the variable
		i = 0;
		minProbe = h.getCapacity();;
		maxProbe = 0;
		sumProbe = 0;

		for (String s: h.getKeyList()){
			i++;
			if (h.getProbe(s) < minProbe) {
				minProbe = h.getProbe(s);
			}
			if (h.getProbe(s) > maxProbe) {
				maxProbe = h.getProbe(s);
			}
			sumProbe = sumProbe + h.getProbe(s);
			if (i == 30) break;
		}

		System.out.println("Minimum Probe: " + minProbe);
		System.out.println("Maximum Probe: " + maxProbe);
		System.out.println("Average Probe: " + String.format("%.3g%n", (float)sumProbe/30));

		System.out.println(String.format("%-6s","S.N.") + String.format("%-17s","Key") + String.format("%-14s", "Init. Hash") + String.format("%-14s", "Hash/Index") + String.format("%-3s","Probe"));
		for (String s: h.getKeyList()){
			System.out.println(String.format("%-6d",h.getValue(s)) + s + " " + String.format("%-14d",h.getiHash(s)) + String.format("%-14d",h.getHash(s)) + h.getProbe(s));
		}

		// OPTION C(C)
		h.resetTable();
		h.fillList(h.getCapacity());
		System.out.println("\nSolution of OPTION C(C) PART I\n");
		for (i = 0; i < h.getCapacity()*0.5; i++) {
			key = list[i];
			h.putValueRandom(key, i+1);
		}

		// reset the variable
		i = 0;
		minProbe = h.getCapacity();;
		maxProbe = 0;
		sumProbe = 0;
		for (String s: h.getKeyList()){
			i++;
			if (h.getProbeRandom(s) < minProbe) {
				minProbe = h.getProbeRandom(s);
			}
			if (h.getProbeRandom(s) > maxProbe) {
				maxProbe = h.getProbeRandom(s);
			}
			sumProbe = sumProbe + h.getProbeRandom(s);
			if (i == 30) break;
		}

		System.out.println("Minimum Probe: " + minProbe);
		System.out.println("Maximum Probe: " + maxProbe);
		System.out.println("Average Probe: " + String.format("%.3g%n", (float)sumProbe/30));

		System.out.println(String.format("%-6s","S.N.") + String.format("%-17s","Key") + String.format("%-14s", "Init. Hash") + String.format("%-14s", "Hash/Index") + String.format("%-3s","Probe"));
		for (String s: h.getKeyList()){
			System.out.println(String.format("%-6d",h.getValueRandom(s)) + s + " " + String.format("%-14d",h.getiHashRandom(s)) + String.format("%-14d",h.getHashRandom(s)) + h.getProbeRandom(s));
		}

		h.resetTable();
		h.fillList(h.getCapacity());
		System.out.println("\nSolution of OPTION C(C) PART II\n");
		for (i = 0; i < h.getCapacity()*0.9; i++) {
			key = list[i];
			h.putValueRandom(key, i+1);
		}

		// reset the variable
		i = 0;
		minProbe = h.getCapacity();;
		maxProbe = 0;
		sumProbe = 0;
		for (String s: h.getKeyList()){
			i++;
			if (h.getProbeRandom(s) < minProbe) {
				minProbe = h.getProbeRandom(s);
			}
			if (h.getProbeRandom(s) > maxProbe) {
				maxProbe = h.getProbeRandom(s);
			}
			sumProbe = sumProbe + h.getProbeRandom(s);
			if (i == 30) break;
		}

		System.out.println("Minimum Probe: " + minProbe);
		System.out.println("Maximum Probe: " + maxProbe);
		System.out.println("Average Probe: " + String.format("%.3g%n", (float)sumProbe/30));

		System.out.println(String.format("%-6s","S.N.") + String.format("%-17s","Key") + String.format("%-14s", "Init. Hash") + String.format("%-14s", "Hash/Index") + String.format("%-3s","Probe"));
		for (String s: h.getKeyList()){
			System.out.println(String.format("%-6d",h.getValueRandom(s)) + s + " " + String.format("%-14d",h.getiHashRandom(s)) + String.format("%-14d",h.getHashRandom(s)) + h.getProbeRandom(s));
		}
	}
}
