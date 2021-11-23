package org.example.streamLearn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class CreateStreamTest {

	@Test
	void streamEmptyTest() {
		/** stream empty test **/

		Stream<String> emptyStream = Stream.empty();
		Stream<String> oneStream = Stream.of("halo");

		String data = null;
		Stream<String> emptyOrNot = Stream.ofNullable(data);
	}

	@Test
	void streamArray() {
		/** stream array **/

		Stream<String> streamString = Stream.of("halo", "dunia", "ini");
		streamString.forEach(System.out::println);

		Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6, 7);
		integerStream.forEach(x -> {
			System.out.println(x);
		});
		String[] arr = new String[]{
				"halo", "dunia", " ini"
		};
		Stream<String> dariArray = Arrays.stream(arr);
		dariArray.forEach(System.out::println);
	}

	@Test
	void streamFromCollection() {
		/** stream collection **/

		Collection<String> collection = List.of("halo", "dunia", "ini");
		Stream<String> stringStream = collection.stream();
		stringStream.forEach(x -> System.out.println(x));
	}

	@Test
	void streamBuilder() {
		/** building with builder **/

		Stream.Builder<String> builder = Stream.builder();
		builder.accept("halo");
		builder.add("halo").add("dunia").add("ini");

		Stream<String> stringStream = builder.build();

		stringStream.forEach(x -> System.out.println(x));
	}

	@Test
	void streamBuilderSimple() {
		/** building with builder **/
		Stream<Object> stream = Stream.builder()
				.add("cika").add("sade").add("winta").build();

		stream.forEach(x -> System.out.println(x));
	}

	@Test
	void streamOperation() {
		/** stream operation MAP == mengubah data yang ada di dalam stream**/
		List<String> nama = List.of("halo", "dunia", "ini"); // collection
		Stream<String> stringStream = nama.stream(); // masukan kedalam stream
		Stream<String> stringUpperCase = stringStream.map(x -> x.toUpperCase());
		stringUpperCase.forEach(s -> System.out.println(s));

		/** list data lama nya tidak berubah, karna stream itu immutable **/
		nama.forEach(System.out::println);
	}

	@Test
	void streamPipeline() {
		/** stream pipeline pasti berkaitan
		 * biasanya seperti ini yang sudah sudah jadi programmer **/
		// cara lama
//		List<String> nama = List.of("halo", "dunia", "ini"); // collection
//		Stream<String> s1 = nama.stream();
//		Stream<String> s2 = s1.map(x -> x.toUpperCase());
//		Stream<String> s3 = s2.map(y -> "mr " + y);
//		s3.forEach(x -> System.out.println(x));

		/** programmer handal pake ini  **/
		/** ingat map disini bertugas untuk memanipulasi data  **/

		List<String> nama = List.of("halo", "dunia", "pipa"); // collection
		nama.stream()
				.map(s -> s.toUpperCase())
				.map(x -> "jika " + x)
				.forEach(y -> System.out.println(y));
	}

	@Test
	void intermediateOperationStream() {
		/** lazy evaluation
		 * operation intermediate atau lazy atau cold ini adalah stream ini tidak akan di eksekusi
		 * jadi jika kita ingin mentrigger si stream ini kita menggunakan terminal operation
		 * yang biasa digunakan adalah for each bukan map **/

		List<String> nama = List.of("halo", "dunia", "pipa");

		nama.stream()
				.map(x -> {
					System.out.println("mengubah " + x + " to uppercase");
					return x.toUpperCase();
				})
				.map(x -> {
					System.out.println("mengubah " + x + " to letsgooo");
					return "letsgoo " + x;
				})
				.forEach(s -> {
					System.out.println(s);
				});
		/** jadi seperti ini maka stream nya akan berjalan **/
		/** dan cara kerja stream ini sendiri adalah menglirkan data
		 * bukan mengalirkan semua data secara bersamaan tapi
		 * stream akan mengalirkan data satu persatu
		 * maka ilustrasi nya seperti ini
		 * stream(halo) -> masuk ke operation map -> to uppercase -> kemudian ke opration for each
		 * cara kerjanya eksekusi satu persatu hingga selesai **/

		nama.forEach(System.out::println); // data asli tidak berubah
	}

	@Test
	void mapOperation() {

		/** map (T-> U) === mengubah stream T menjadi stream U
		 * dan map ada return nya */

		List<String> nama = List.of("halo", "dunia", "pipa");

		nama.stream()
				.map(x -> x.toUpperCase())
				.map(y -> y.length())
				.forEach(z -> System.out.println(z));
		/** mengubah stream string ke stream integer **/

	}

	@Test
	void flatMap() {
		/** *  flatMap (T -> Stream<U> mengubah stream T menjadi Stream yang lain U
		 *  kemudian otomatis dimerging jadi satu data */

		List<String> nama = List.of("halo", "dunia", "pipa");
		nama.stream()
				.map(x -> x.toUpperCase())
				.flatMap(y -> Stream.of(y, y.length()))
				.flatMap(v -> Stream.of(v, v, v))
				.forEach(z -> System.out.println(z));

	}

	@Test
	void filteringOperation() {
		/** operati filter ini adalah untuk memilah milah mana data yang akan dipilih
		 * filter (U-Boolean) === mengambil data yang masuk di kriteria filter
		 */
		List<String> nama = List.of("halo", "dunia", "pipa", "tokojawa", "alexirumi", "pipa");
		List<Integer> integers = List.of(1, 2, 4, 5, 6, 7, 8, 9);

		nama.stream().filter(x -> x.length() > 5).forEach(System.out::println);
		integers.stream().filter(x -> x % 2 == 0).forEach(System.out::println);
	}

	@Test
	void distinctOperation() {
		/** distinct () === menghapus semua data yang duplicate di dalam stream **/

		List<String> nama = List.of("halo", "dunia", "pipa", "tokojawa", "alexirumi", "pipa");
		nama.stream().distinct().forEach(System.out::println);
	}

	@Test
	void retrievingOperation() {
		/** pengambilan sebagian data
		 * limit(n) mengambil sejumlah data n
		 * skip(n) menghiraukan sejumlah data pertama n
		 * takeWhile(T -> boolean) mengambil data  seperti filter tetapi ini hanya selama kondisi true dan ketika false dia stop
		 * dropWhilr(T -> boolean) menghiraukan data selama kondisi true
		 * */

		List<String> nama = List.of("halo", "dunia", "pipa", "tokojawa", "alexirumi", "pipa");
		System.out.println("========limit=========");
		nama.stream().limit(2).forEach(System.out::println);
		System.out.println("========skip=========");
		nama.stream().skip(2).forEach(System.out::println);
		System.out.println("========takeWhile=========");
		nama.stream().takeWhile(x -> x.length() <= 5).forEach(System.out::println);
		System.out.println("========dropWhile=========");
		nama.stream().dropWhile(x -> x.length() <= 5).forEach(System.out::println);
	}

	@Test
	void sortedOperation() {
		List<String> nama = List.of("halo", "dunia", "pipa", "tokojawa", "alexirumi", "pipa");
		nama.stream().sorted().forEach(System.out::println);
	}

	@Test
	void sortedComparatorOperation() {
		Comparator<String> reverse = Comparator.reverseOrder();

		List<String> nama = List.of("halo", "dunia", "pipa", "tokojawa", "alexirumi", "pipa");
		nama.stream().sorted(reverse).forEach(System.out::println);
	}

	@Test
	void aggregateOperation() {
		List<String> nama = List.of("halo", "dunia", "pipa", "tokojawa", "alexirumi", "pipa");
		nama.stream().max(Comparator.naturalOrder()).ifPresent(System.out::println);

		nama.stream().min(Comparator.naturalOrder()).ifPresent(System.out::println);
		long names = List.of("halo", "dunia", "pipa", "tokojawa", "alexirumi", "pipa")
				.stream().count();
		System.out.println(names);
	}

	@Test
	void manualAggregateReduce() {
		/** digunakan untuk proses aggregate secara manual	 */

		int num = List.of(1, 2, 3, 4, 5).stream().reduce(0, (value, item) -> value + item);
		// iterasi ke 0
		// iterasi ke 1. value = 0 item = 1 = hasil = 1
		// iterasi ke 2. value = 1 item = 2 = hasil = 3
		// iterasi ke 3. value = 3 item = 3 = hasil = 6
		// iterasi ke 4. value = 6 item = 4 = hasil = 10
		// iterasi ke 5. value = 10 item = 5 = hasil = 15

		System.out.println(num);
	}

	@Test
	void manualAggregateFactorial() {
		/** digunakan untuk proses aggregate secara manual	 */

		int factorial = List.of(1, 2, 3, 4, 5).stream().reduce(1, (value, item) -> value * item);
		// iterasi ke 0
		// iterasi ke 1. value = 0 item = 1 = hasil = 1
		// iterasi ke 2. value = 1 item = 2 = hasil = 2
		// iterasi ke 3. value = 2 item = 3 = hasil = 6
		// iterasi ke 4. value = 6 item = 4 = hasil = 24
		// iterasi ke 5. value = 10 item = 5 = hasil = 120

		System.out.println(factorial);
	}

	@Test
	void checkOperation() {
		/** operasi yang digunakan untuk melakukan pengecekan didalam stream
		 * anyMatch (T -> boolean) apakah ada salah satu data yang match dengan kondisi yang di input
		 * allMatch (T -> boolean) harus semua sama dengan kondisi
		 * noneMatch (T -> boolean) tidak ada yang sama dengan kondisi
		 * */

		boolean anyMatch = List.of(1, 2, 3, 4, 9, 5).stream()
				.anyMatch(number -> number > 7);
		boolean allMatch = List.of(1, 2, 3, 4, 9, 5).stream()
				.allMatch(number -> number > 0);
		boolean noneMatch = List.of(1, 2, 3, 4, 9, 5).stream()
				.noneMatch(number -> number > 9);
		System.out.println("========any match=======");
		System.out.println(anyMatch);
		System.out.println("========all match=======");
		System.out.println(allMatch);
		System.out.println("========none match=======");
		System.out.println(noneMatch);
	}

	@Test
	void forEachOperation() {
		/** forEach(T -> void ) kembalinya adalah void dan dia adalah terminal operation
		 * peek(T -> void ) melakukan iterasi satu persatu sama dengan foreach namun mengembalika stream lagi
		 * dan peek bukanlan terminal operation */

		List<String> name = List.of("halo", "hari", "disana", "ada", "joko");
		//menggunakan map
		name.stream().map(x -> {
			String upper = x.toUpperCase();
			System.out.println("mengubah nama menjadi huruf besar : " + upper);
			return upper;
		}).forEach(x -> System.out.println("hasil akhir nya seperti ini : " + x));

		System.out.println("=============================================");

		// peek operation
		name.stream().peek(x -> System.out.println("sebelum diubah : " + x))
				.map(x -> x.toUpperCase())
				.peek(x -> System.out.println("setelah diubah menjadi huruf besar : " + x))
				.forEach(x -> System.out.println("hasil akhirnya : " + x));
	}

	@Test
	void primitiveStream() {
		IntStream intStream = IntStream.of(1, 112);
		intStream.forEach(System.out::println);

		LongStream longStream = LongStream.of(1,2,3,4,5);
		longStream.forEach(System.out::println);

		DoubleStream doubleStream = DoubleStream.builder().add(2.2).add(1.1).add(3.3).build();

	}


	Stream<String> getStream(){
		return Stream.of("halo", "hari", "disana", "ada", "joko");
	}
	@Test
	void collectorOperation() {
		/** biasanya digunakan untuk mengcollect data stream dan kemudian dapat diubah menjadi
		 * struktur data yang diinginkan, biasanya Stream menjadi Collection
		 */

		/** Collectors adalah sebuah class helper yang biasa digunakan untuk membuat collector
		 * ini dapat mempermudah kita jika ingin melakukan operasi collect terhadap sebuah stream
		 */

		/** MENGKONVERSI STREAM MENJADI COLLECTION */
		Set<String> set = getStream().collect(Collectors.toSet());
		Set<String> immutableSet = getStream().collect(Collectors.toUnmodifiableSet());

		List<String> list = getStream().collect(Collectors.toList());
		List<String> immutableList = getStream().collect(Collectors.toUnmodifiableList());
	}

	@Test
	void mapCollectors() {
		/** map(K,V) */
		List<String> name = List.of("halo", "hari", "disana", "ada", "joko");
		Map<String, Integer> map = name.stream().collect(Collectors.toMap(
				x -> x, x-> x.length()
		));
		System.out.println(map);

	}

	@Test
	void groupingOperation() {
		Map<String, List<Integer>> map = Stream.of(1,2,3,4,5,6,7,8,99)
				.collect(Collectors.groupingBy(x-> {
					if(x > 5 ){
						return "besar";
					} else {
						return "kecil";
					}
				}));

		System.out.println(map);

		Map<String, List<String>> mapString = Stream.of("hari", "joko", "karyo", "sudirman")
				.collect(Collectors.groupingBy(x-> {
					if(x.length() > 4 ){
						return "nama panjang";
					} else {
						return "nama pendek";
					}
				}));
		System.out.println(mapString);
	}

	@Test
	void partitioningOperation() {
		Map<Boolean, List<Integer>> map = Stream.of(1,2,3,4,5,6,7,8,99)
				.collect(Collectors.partitioningBy(x-> {
					return x > 5;
				}));

		System.out.println(map);

		Map<Boolean, List<String>> mapString = Stream.of("hari", "joko", "karyo", "sudirman")
				.collect(Collectors.groupingBy(x-> {
					return x.length() > 4;
				}));
		System.out.println(mapString);
	}

}
