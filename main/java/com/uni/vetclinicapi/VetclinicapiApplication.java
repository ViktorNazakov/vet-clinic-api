package com.uni.vetclinicapi;

import com.uni.vetclinicapi.persistance.entity.Visit;
import com.uni.vetclinicapi.service.dto.VisitDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@SpringBootApplication
public class VetclinicapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetclinicapiApplication.class, args);
	}

}
