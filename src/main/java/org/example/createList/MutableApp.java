package org.example.createList;

import java.util.List;

public class MutableApp {
	public static void main(String[] args) {
		Person person = new Person("joko");

		person.addHobby("game");
		person.addHobby("code");

		for (String res : person.getHobbies()){
			System.out.println(res);
		}
	}

	public static void doSomething(List<String> hobbies){

	}

}
