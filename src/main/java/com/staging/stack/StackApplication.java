package com.staging.stack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.time.format.DateTimeFormatter;

@SpringBootApplication

public class StackApplication {



	public static void main(String[] args) throws ParseException {

		SpringApplication.run(StackApplication.class, args);
	}

}
