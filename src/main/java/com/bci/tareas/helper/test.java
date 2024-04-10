package com.bci.tareas.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

	public static void main(String[] args) {
		String password = "aA123aA$@456";
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

		Matcher matcher = pattern.matcher(password);
		System.out.println(matcher.matches());
	}

}
