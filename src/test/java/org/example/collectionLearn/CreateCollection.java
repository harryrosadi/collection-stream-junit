package org.example.collectionLearn;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

		for (String result : col) {
			System.out.println(result);
		}
		/** contains() digunakan untuk pengecekan data di dalam list bersifat boolean
		 * size() mengecek ada berapa list yang ada **/
		System.out.println("CONTAIN");
		System.out.println(col.contains("sekolah"));
	}

	@Test
	void listInterface() {
		/** list adalah struktur data yang memiliki sifat
		 * element yang di list dapat duplikat, bisa memasukan data yang sama
		 * data list berurut sesuat dengan posisi kita memasukan data
		 * list memiliki index mirip dengan array, sehingga kita dapat menggunakan nomor index untuk mengambil data
		 * list adalah turunan dari collection */

		/** ArrayList implementasi dari list menggunakan array
		 * default nya kapasitas array di ArrayList ini adalah 10
		 * tetapi jika kita memasukan data ke 11 maka secara otomatis ArrayList akan
		 * membuat array baru dengan kapasitas baru (MAGIC) */

		/** LinkedList adalah implementasi dengan struktur data Double LinkedLiss
		 * double Linkklist adalah data disimpan dalam bentuk node dimana ada pointer Next dan Prev dan data akhir Next null
		 * dan tidak ada indexing di LInkedList */

		/** Operasi 	ArrayList						LinkedList
		 *  add	->		cepat jika array masih cukup,	cepat karna hanya menambah node diakhir
		 *  			lambat jika penuh
		 *  get	->		cepet karna menggunakan index	lambat karna harus cek node dari awal
		 *  											hingga ketemu indexnya
		 *  set	->		cepat karna menggunakan index	lambat karna harus cek node dari awal
		 *  											sampai ketemu indexnya
		 *  remove -> 	lambat karna harus menggeser 	cepat karna mengubah prev dan next
		 *  			data dibelakang yang dihapus
		 */

		/** ArrayList(initialCapacity default = 10) jika ingin lebih cepat dapat diubah sesuai kebutuhan kita
		 * sehingga ArrayList tidak perlu membuat ArrayList baru lagi. */
		List<String> strings = new ArrayList<>();
		List<String> stringList = new ArrayList<>();

		strings.add("halo");
		strings.add("joko");
		strings.set(0, "sudirman");

		for (String res : strings) {
			System.out.println(res);
		}
	}

	@Test
	void mutableImumtable() {
		List<String> mutable = new ArrayList<>();
		mutable.add("joko");
		mutable.add("karyo");
		mutable.add("sudirman");

		/** ini sama dengan */
		List<String> immutable = Collections.unmodifiableList(mutable);
		for(String res : mutable){
			System.out.println(res);
		}
		/** ini  */
		List<String> tidakBisa = List.of("joko", "sudirman", "karyo");
//		tidakBisa.add("halo"); // pasti error exception
	}
}
