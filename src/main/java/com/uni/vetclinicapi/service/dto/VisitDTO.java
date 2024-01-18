package com.uni.vetclinicapi.service.dto;

import com.uni.vetclinicapi.persistance.entity.Pet;
import com.uni.vetclinicapi.persistance.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
import java.sql.Time;

@Schema(description = "Visit DTO is used for carrying information about the Visit Entity.", allowableValues = {"date", "time", "description", "pet","vet"})
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Validated
public class VisitDTO {

    @Schema(description = "Date of the visit.",
            example = "2007-12-03")
    @NotNull(message = "Date should be specified.")
    private Date date;

    @Schema(description = "Time of the visit.",
            example = "15:30")
    @NotNull(message = "Time should be specified.")
    private Time time;

    @Schema(description = "Description/summery of the visit.")
    @NotNull(message = "Description should be specified.")
    @Size(min = 10, max = 100, message = "Description should be of length from 10 to 100 symbols.")
    private String description;

    @Schema(description = "Pet of the User(Owner).",
            example = "480d68e4-3f13-48f0-a6ec-c94b4d0d7683")
    @NotNull(message = "Pet should be specified.")
    private Pet pet;

    @Schema(description = "Vet that should check the pet.",
            example = "480d68e4-3f13-48f0-a6ec-c94b4d0d7683")
    @NotNull(message = "Vet  should be specified.")
    private User vet;
}
