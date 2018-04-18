package anothertest.alg0;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
class Ans {
	private static class SubSet<T> {
		List<T> items; // items of subset
		int startIndex1; // start index in list 1
		int endIndex1; // end index in list 1
		int startIndex2; // start index in list 2
		int endIndex2; // end index in list 2
	}

	public HashMap<String, Integer> totalOps = new HashMap<String, Integer>() {{ put("replace", 0); put("delete", 0); put("insert", 0); }};
	public int totalSkipped = 0;
	List<Integer> hand, goal;
	
	public Ans(ArrayList<String> seq, int k) {
		hand = seq.subList(0, k+1).stream().map(Integer::parseInt).collect(Collectors.toList());
		goal = seq.subList(k+1, seq.size()).stream().map(Integer::parseInt).collect(Collectors.toList());
		disperse();
	}
	
	void disperse() {
		while (hand.size() > 0 && goal.size() > 0 && hand.get(0) == goal.get(0)) {
			hand.remove(0);
			goal.remove(0);
			totalSkipped++;
		}
	}

	SubSet<Integer> biggestSublist() {
		SubSet<Integer> output = null;

		for (int i = 0; i < hand.size(); i++) {
			for (int j = 0; j < goal.size(); j++) {

				if (output != null && output.items.size() > Math.min(hand.size(), goal.size())) {
					return output;
				}

				if (hand.get(i).equals(goal.get(j))) {
					// inspect sub sequence from this (i,j) onwards
					output = inspectSubSet(hand, goal, i, j, output);
				}
			}
		}

		return output;
	}

	private static <T> SubSet<T> inspectSubSet(List<T> list1, List<T> list2, int index1, int index2,
			SubSet<T> oldSubSet) {
		// new subset candidate
		SubSet<T> newSubSet = new SubSet<T>();
		newSubSet.items = new ArrayList<T>();
		newSubSet.startIndex1 = index1;
		newSubSet.endIndex1 = index1;
		newSubSet.startIndex2 = index2;
		newSubSet.endIndex2 = index2;

		// keep building subset as subsequent items keep matching
		do {
			newSubSet.items.add(list1.get(index1));
			newSubSet.endIndex1 = index1;
			newSubSet.endIndex2 = index2;
			index1++;
			index2++;
		} while (index1 < list1.size() && index2 < list2.size() && list1.get(index1).equals(list2.get(index2)));

		// return first, larger or same.
		if (oldSubSet == null) {
			return newSubSet;
		} else if (newSubSet.items.size() > oldSubSet.items.size()) {
			return newSubSet;
		} else {
			return oldSubSet;
		}
	}
	public void emitDelete(int index) {
		System.out.println("Delete element at idx#" + (index + totalSkipped) + " (" + hand.get(index) + ")");
		totalOps.put("delete", totalOps.get("delete")+1);
	}
	public void emitInsert(int index, int value) {
		System.out.println("Insert at idx#" + (index + totalSkipped) + " : " + value);
		totalOps.put("insert", totalOps.get("insert")+1);
	}
	public void emitReplace(int index) {
		System.out.println("Replace idx#" + (index + totalSkipped) + " (" + hand.get(index) + " -> " + goal.get(index) + ")");
		totalOps.put("replace", totalOps.get("replace")+1);
	}
	public void solve() {
		SubSet<Integer> subset;
		do {
			subset = biggestSublist();
			
			if(subset == null) break;
			System.out.println(subset.items.toString());
			
			// match pre-sublist
			if(subset.startIndex2 > subset.startIndex1) {
				for(int i = subset.startIndex1; i < subset.startIndex2; i++) {
					emitDelete(i);
					hand.remove(subset.startIndex1);
				}
			}
			if(subset.startIndex2 < subset.startIndex1) {
				for(int i = 0; i < subset.startIndex1 - subset.startIndex2; i++) {
					emitDelete(i);
					hand.remove(subset.startIndex1);
				}
			}
			// match the head
			for(int i=0; i<subset.startIndex1; i++)
				emitReplace(i);
			
			hand = hand.subList(subset.endIndex1, hand.size());
			goal = goal.subList(subset.endIndex2, goal.size());
			
		} while(subset.items.size() > 0);
		
		while(goal.size() > 0 && hand.size() > 0) {
			emitReplace(0);
			hand.remove(0);
			goal.remove(0);
		}
		while(goal.size() > 0) {
			int a = goal.remove(0);
			emitInsert(0, a);
			hand.add(0, a);
		}
		while(hand.size() > 0) {
			emitDelete(0);
			hand.remove(0);
		}
	}
}

public class Main {
	public static void main(String[] args) throws IOException {
		Ans ans;
		int k;
		ArrayList<String> params = new ArrayList<>();
		if (args.length >= 2) {
			for(String arg : args)
				params.add(arg);
			k = Integer.parseInt(params.remove(params.size() - 1));
 		} else {
 			System.out.println("Enter thy sequence: ");
 			Scanner scanner = new Scanner(System.in);
 			
 			String s = scanner.next();
 			for(char sc : s.toCharArray()) {
 				params.add(sc + "");
 			}
 			
 			System.out.println("Enter thy K const: ");
 			k = scanner.nextInt();
 		}
		ans = new Ans(params, k);
		ans.solve();
		
		System.out.println("\nTotal Operations: ");
		for(Map.Entry<String, Integer> e : ans.totalOps.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
	}
}
