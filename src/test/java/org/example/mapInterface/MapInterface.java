package org.example.mapInterface;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class MapInterface {

	@Test
	void mapOperation() {
		/** map adalah struktur data yang berisikan antara key dan value
		 * dimana key map itu harus unik, tidak bisa duplikat dan satu key hanya boleh mapping ke satu value
		 * jika duplikat akan otomatis replace data dengan key yang sama(ditimpa)
		 * map ini mirip Array, kalau array key = int tetapi kalo Map key = suka suka kita
		 */

	}

	@Test
	void hashMapOperation() {
		/** HashMap adalah implementasi Map yang melakukan distribusi key menggunakan hashCode() function
		 * karena HashMap sangat bergantung dengan hashCode(), jadi pastikan harus membuat hashCode seunik mungkin
		 * karena jika terlalu banuak nilai hashCode() yang sama, maka pendistribusian key tidak optimal
		 * sehingga untuk get data di Map akan lambat
		 * HashMap pengecekan data duplikat dilakukan dengan menggunakan method equals
		 */

		Map<Integer, String> map = new HashMap<>();
		map.put(1, "halo");
		map.put(2, "joko");
		map.put(3, "disini");

		System.out.println(map.get(2));
	}

	@Test
	void weakHashMap() {
		/** WeakHash adalah implementasi Map mirip dengan HashMap
		 * bedanya dengan HashMap, ini menggunakan weak key, dimana jika tidak digunakan
		 * lagi maka secara otomatis data yang ada di WeakHashMap akan dihapus
		 * jika terjadi Garbage collection di Java, bisa jadi data di weakHashmap akan dihapus
		 * weakHasMap cocok untuk menyimpan data cache di memori
		 */

		Map<Integer, Integer> map = new WeakHashMap<>();
//		Map<Integer, Integer> map = new HashMap<>();
		/** data yang 100 ribu ini jika menggunakan weakHashMap tidak akan utuh
		 * jika garbage collector dijalankan
		 * kalau HashMap dia kuat
		 */
		for (int i = 0; i < 200000; i++) {
			map.put(i, i);
		}
		System.gc();
		System.out.println(map.size());
	}

	@Test
	void identityHashMap() {
		/** identitiyHashMap tidak menggunakan equals, melainkan menggunakan ==
		 * artinya data dianggap sama jika lokasi memori tersebut sama
		 */
		Map<String, String> map = new IdentityHashMap<>();
		String key1 = "satu.dua";

		String satu = "satu";
		String dot = ".";
		String dua = "dua";

		String key2 = satu + dot + dua;

		map.put(key1, "halo");
		map.put(key2, "jooko");

		System.out.println(key1.equals(key2));

		System.out.println(key1 == key2);

		System.out.println(map.size());
	}

	@Test
	void linkedHashMap() {
		/** data di LinkedHashMap dapat terprediksi karena datanya disimpan berurutan
		 * dalam linked list sesuai dengan urutan kita menyimpan data
		 * proses get data di LinkedHashMap akan lambat karena harus melalukan iterasi data linkedList terlebih dahulu
		 * gunakan LinkedHashMap jika memang mementingkan iterasi data Map nya saja */

		Map<Integer, String> map = new LinkedHashMap<>();
		map.put(1, "halo");
		map.put(2, "dua");
		map.put(3, "joko");

		Set<Integer> keys = map.keySet();
		for(Integer res : keys){
			System.out.println(res);
		}
		System.out.println(map);
	}

	@Test
	void sortedMap() {
		SortedMap<Integer, String> stringSortedMap = new TreeMap<>();
		stringSortedMap.put(1,"halo");
		stringSortedMap.put(3,"bikaalo");
		stringSortedMap.put(2,"kalo");
		for (Integer res : stringSortedMap.keySet()){
			System.out.println(res);
		}

		/** menggunakan comparator */
		Comparator<String> stringComparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		};
		Comparator<Integer> integerComparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		};

		SortedMap<String, String> compare = new TreeMap<>(stringComparator);
		compare.put("mungkin","halo");
		compare.put("harus","bikaalo");
		compare.put("string","kalo");
		for (String res : compare.keySet()){
			System.out.println(res);
		}
		SortedMap<String, String> immutable = Collections.unmodifiableSortedMap(compare);

	}

	@Test
	void navigableMap() {
		NavigableMap<Integer,String> map = new TreeMap<>();
		map.put(1,"map");
		map.put(2,"norak");
		map.put(3,"halo");
		map.put(4,"joko");
		map.put(5,"sudirman");

		for(Integer res : map.keySet()){
			System.out.println(res);
		}

		NavigableMap<Integer,String> mapDesc = map.descendingMap();
		for(Integer res : mapDesc.keySet()){
			System.out.println(res);
		}

		System.out.println(map.lowerKey(3));
		System.out.println(map.higherKey(4));

	}

	@Test
	void entryMap() {

		Map<Integer, String> map = new HashMap<>();
		map.put(1, "halo");
		map.put(2, "dua");
		map.put(3, "joko");

		Set<Map.Entry<Integer, String>> entrySet = map.entrySet();
		for(Map.Entry<Integer, String> res : entrySet){
			System.out.println(res.getKey() + " : " + res.getValue());
		}
		System.out.println(map);
	}

	void groupingby(){
		List<Object> itemOutlet = List.of(1,2,3,4);
//		Map<Integer, List<Object>> posOutletMap = itemOutlet.stream().collect(Collectors.groupingBy(Object::getClass));
		Integer x = null;
		String x5 = null;
		Map<Integer, List<String>> map = new HashMap<>();
		if (!map.containsKey(x)){
			map.put(x, new ArrayList<>(List.of("2","3")));
		}else {
			List<String> xx = map.get(x);
			xx.add(x5);
			map.put(x, xx);

		}

		for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
			Integer key = entry.getKey();
			List<String> value = entry.getValue();

		}
	}
}

