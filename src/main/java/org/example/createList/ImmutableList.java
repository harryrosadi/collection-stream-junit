package org.example.createList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImmutableList {
	public static void main(String[] args) {
		List<String> mutable = new ArrayList<>();
		mutable.add("joko");
		mutable.add("karyo");
		mutable.add("sudirman");

		/** ini sama dengan */
		List<String> immutable = Collections.unmodifiableList(mutable);
		/** ini  */
		List<String> tidakBisa = List.of("joko", "sudirman", "karyo");
		tidakBisa.add("halo"); // pasti error exception
	}
}
