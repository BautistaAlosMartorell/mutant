package com.example.mutant.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to validate DNA sequences for mutant detection.
 */
@Documented
@Constraint(validatedBy = DnaValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDna {
    String message() default "DNA sequence is not valid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
