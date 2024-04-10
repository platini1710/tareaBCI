package com.bci.tareas.test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bci.tareas.controllers.ControllerConsulta;
import com.bci.tareas.helper.PasswordValidator;



public class PasswordValidatorTest {
	private static final Logger logger = LoggerFactory.getLogger(PasswordValidatorTest.class);
	
	@ParameterizedTest
    @MethodSource("validPasswordProvider")
    public void test_password_regex_valid(String password) {
		logger.info("password  validas =" + password);
    	assertTrue(PasswordValidator.isValid(password));
       
    }

    @ParameterizedTest(name = "#{index} - Run test with password = {0}")
    @MethodSource("invalidPasswordProvider")
    void test_password_regex_invalid(String password) {
    	logger.info("password no validas ="+password );
    	assertFalse(PasswordValidator.isValid(password));
    }

     static Stream<String> validPasswordProvider() {
        return Stream.of(
                "A23bbbccc@",
                "Hello@12",
                "A!@#2&()aa1",              // test punctuation part 1
                "A[{2@}]:;a",           // test punctuation part 2
                "A7$^+=2a#",               // test symbols
                "A89$abcdeab",     // test 20 chars
                "12fAa$wa"                  // test 8 chars
        );
    }

    // At least
    // one lowercase character,
    // one uppercase character,
    // one digit,
    // one special character
    // and length between 8 to 20.
    static Stream<String> invalidPasswordProvider() {
        return Stream.of(
                "12345678",                 // invalido, solo digit0s
                "abcdefgh",                 // invalido, only minusculas
                "ABCDEFGH",                 // invalido, only mayusculas
                "abc123$$$",                // invalido, al menos una mayuscula
                "ABC123$$$",                // invalido, al menos una minuscula
                "ABC$$$$$$",                // invalido, al menos una digito
                "java REGEX 123",           // invalido, al menos una caracter especial
                "java REGEX 123 %",         // invalido, % no es caracter especiall
                "________",                 // invalido
                "--------",                 // invalido
                " ",                        //invalido  vacio
                "Asds23");                        //invalido empty
    }
  
}