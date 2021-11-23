package org.example.createList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Person {
	String name;
	List<String> hobbies;

	public Person(String name) {
		this.name = name;
		this.hobbies = new ArrayList<>();
	}

	public void addHobby(String hobby) {
		hobbies.add(hobby);
	}

	public String getName() {
		return name;
	}

	/**
	 * unmodifiableList digunakan untuk list tidak dapat diubah
	 */
	public List<String> getHobbies() {
		return Collections.unmodifiableList(hobbies);
	}
}
