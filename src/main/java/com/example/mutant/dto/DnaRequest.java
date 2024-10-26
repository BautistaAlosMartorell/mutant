package com.example.mutant.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import com.example.mutant.validators.ValidDna;

@Getter
@Setter
public class DnaRequest {
    @ValidDna
    private String[] dna;

}