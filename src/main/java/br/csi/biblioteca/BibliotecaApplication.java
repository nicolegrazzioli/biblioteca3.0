package br.csi.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);

//		System.out.println("HASH SENHA ADMIN: " + new BCryptPasswordEncoder().encode("admin123"));
//		System.out.println("HASH SENHA USUARIO: " + new BCryptPasswordEncoder().encode("usuario123"));
	}

}
