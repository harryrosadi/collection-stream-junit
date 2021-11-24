package org.example.collectionLearn;


import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

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

	@Test
	void setInterface() {
		/** Set<> adalah salah satu collection yang berisikan unique element atau tidak boleh duplikat
		 * Set tidak memiliki index, oleh karena itu Set tidak berurutan
		 * Set tidak memiliki method baru, jadi hanya menggunakan dari Collection dan iterable
		 * jadu untuk mengambil data dari Set kita perlu melakukan iterasi satu per satu */

		/** HashSet LinkedHashSet sebenernya sama sama ada hash table, dimana disimpan
		 * dalam sebuah hash table dengan mengkalkulasi hashCode() function
		 * HashSet tidak menjamin data berurut sesuai dengan waktu kita menambah data
		 * LinkedHashSet menjamin data berurut sesuati dengan waktu kita menambah data
		 * urutan di LinkedHashSet bisa dijaga karena menggunakan double Linked List */

		/** hasilnya tidak berurutan */
		Set<String> stringSet = new HashSet<>();
		stringSet.add("joko");
		stringSet.add("karyo");
		stringSet.add("sudirman");
		stringSet.add("sudirman"); // hanya 1 yang akan di input

		for (String res : stringSet){
			System.out.println(res);
		}

		Set<String> linkedHash = new LinkedHashSet<>();
		linkedHash.add("mumun");
		linkedHash.add("olop");
		linkedHash.add("halo");

		for (String res : linkedHash){
			System.out.println(res);
		}
	}

	@Test
	void navigableSet() {
		NavigableSet<String> nama = new TreeSet<>();
		nama.addAll(Set.of("hari","tokoku", "hoko","sudirman", "alex", "rumi" , "zeus"));

		NavigableSet<String> descendingSet= nama.descendingSet();
		NavigableSet<String> sudirman = nama.tailSet("sudirman", true);
		NavigableSet<String> sudirman2 = nama.headSet("sudirman", true);

//		for (String x : sudirman){
//			System.out.println(x);
//		}
		for (String x : sudirman2){
			System.out.println(x);
		}
//		for (String x : descendingSet){
//			System.out.println(x);
//		}
	}

	@Test
	void queueInterface() {
		/** queue adalah data yang pertama kali masuk dia harus pertama kali keluar */
		/** ArrayDeque bertugas untuk menggunakan arrya sebagai implemantasi queue nya
		 * LinkedList menggunakan double linked list sebagai implementasi queue nya
		 * PriorityQueue menggunakan array sebagai implementasi queue, namun diurutkan menggunakan
		 * Comparable atau Comparator dan hasil nya berurut
		 */
		Queue<String> queue = new ArrayDeque<>();
		queue.add("joko");
		queue.add("karyo");
		queue.add("alex");
		for(int i = 0; i< 10; i++){
			queue.add(String.valueOf(i));
		}
		for (String x = queue.poll(); x != null; x = queue.poll()){
			System.out.println(x);
		}

		Queue<String> prior = new PriorityQueue<>();
		prior.add("joko");
		prior.add("karyo");
		prior.add("alex");
		for(int i = 0; i< 10; i++){
			queue.add(String.valueOf(i));
		}
		for (String x = prior.poll(); x != null; x = prior.poll()){
			System.out.println(x);
		}

	}

	@Test
	void dequeInterface() {
		/** Deque singkatan dari double ended queue, ini dapat beroprasi dari depan atau belakang
		 * jadi ini lebih mirip seperti tumpukan buku, atau stack
		 */

		Deque<String> strings = new LinkedList<>();
		strings.offer("joko");
		strings.offerFirst("karyo");
		strings.offer("halo");
		strings.offerLast("surimoro");

		for (String res : strings){
			System.out.println(res);
		}
		System.out.println("======================");
		/** ambil data dari last */
		System.out.println(strings.pollLast());
		System.out.println(strings.pollLast());
		System.out.println(strings.pollLast());

		Deque<String> stringLinkedList = new LinkedList<>();
		strings.offer("joko");
		strings.offerFirst("karyo");
		strings.offerLast("surimoro");
		strings.offer("halo");

		/** ambil data dari first */
		System.out.println(stringLinkedList.pollFirst());
		System.out.println(stringLinkedList.pollFirst());
		System.out.println(stringLinkedList.pollFirst());

	}

	@Test
	void sortingList() {
		List<String> sort = new ArrayList<>();

		sort.add("karyo");
		sort.add("halo");
		sort.add("sudirman");
		sort.add("joko");

		Collections.sort(sort);

		for(String res : sort){
			System.out.println(res);
		}

		List<String> descending = new ArrayList<>();

		descending.add("karyo");
		descending.add("halo");
		descending.add("sudirman");
		descending.add("joko");

		Comparator<String> comparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		};

		Collections.sort(descending, comparator);

		for(String res : descending){
			System.out.println(res);
		}
	}
}
