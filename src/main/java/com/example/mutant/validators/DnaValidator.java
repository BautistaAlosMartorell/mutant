package com.example.mutant.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class DnaValidator implements ConstraintValidator<ValidDna, String[]> {

    private static final Set<Character> ALLOWED_CHARACTERS = Set.of('A', 'T', 'G', 'C');

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {

        if (dna == null || dna.length == 0) {
            addViolationMessage(context, "The DNA array cannot be null or empty.");
            return false;
        }

        int size = dna.length;

        for (String row : dna) {
            // Valida que cada fila tenga la longitud correcta
            if (row == null || row.length() != size) {
                addViolationMessage(context, "The DNA array must be NxN.");
                return false;
            }

            // Valida que la fila solo contenga los caracteres permitidos
            for (char nucleotide : row.toCharArray()) {
                if (!ALLOWED_CHARACTERS.contains(nucleotide)) {
                    addViolationMessage(context, "DNA sequences must only contain 'A', 'T', 'G', or 'C'.");
                    return false;
                }
            }
        }

        return true;
    }

    // para poner mensajes de violación personalizados
    private void addViolationMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }


}
