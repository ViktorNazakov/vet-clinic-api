package com.uni.vetclinicapi.service.dto;

import com.uni.vetclinicapi.persistance.entity.Pet;
import com.uni.vetclinicapi.persistance.entity.User;
import com.uni.vetclinicapi.persistance.entity.Vet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Full Visit DTO is used for carrying information about the Visit Entity (ID included).", allowableValues = {"id", "dateAndHour", "description", "pet","vet"})
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Validated
public class FullVisitDTO {

    @Schema(description = "Id of the Visit.", example = "480d68e4-3f13-48f0-a6ec-c94b4d0d7683")
    private UUID id;

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
            example = "Coco")
    @NotNull(message = "Pet should be specified.")
    private Pet pet;

    @Schema(description = "User that owns the pet.",
            example = "Martin")
    @NotNull(message = "Vet should be specified.")
    private Vet vet;

    @Schema(description = "User that makes the visit")
    @NotNull(message = "Viktor")
    private User user;
}
