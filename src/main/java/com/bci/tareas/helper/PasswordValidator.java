package com.bci.tareas.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordValidator {



	private static final Logger logger = LoggerFactory.getLogger(PasswordValidator.class);
	public static boolean isValid(String password) {
		logger.info("password : " +password);
		logger.info("PatronPassword : " + Constantes.PASSWORD_PATTERN);

	    Pattern pattern = Pattern.compile(Constantes.PASSWORD_PATTERN);
	    Matcher matcher = pattern.matcher(password);

		return matcher.matches();
	}

}