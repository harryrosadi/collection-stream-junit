package org.example.collectionLearn;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CreateCollection {

	@Test
	void addToCollection() {
		Collection<String> col = new ArrayList<>();

		col.add("halo");
		col.add("jika");
		col.add("saya");
		col.add("mengubah");
		col.addAll(List.of("saya", "mau", "punya", "sekolah"));

		for(String result : col){
			System.out.println(result);
		}
		/** contains() digunakan untuk pengecekan data di dalam list bersifat boolean
		 * size() mengecek ada berapa list yang ada **/
		System.out.println("CONTAIN");
		System.out.println(col.contains("sekolah"));
	}

	@Test
	void listInterface() {
		/** list adalah struktur data yang memiliki sifat*/
	}
}
