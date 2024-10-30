package com.xiaodao.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ScriptConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptConstraint {

    String message() default "{org.hibernate.validator.constraints.ScriptAssert.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The name of the script language used by this constraint as
     * expected by the JSR 223 {@link javax.script.ScriptEngineManager}. A
     * {@link javax.validation.ConstraintDeclarationException} will be thrown upon script
     * evaluation, if no engine for the given language could be found.
     */
    String lang() default "qlExpress";

    /**
     * @return The script to be executed. The script must return
     * {@code Boolean.TRUE}, if the annotated element could
     * successfully be validated, otherwise {@code Boolean.FALSE}.
     * Returning null or any type other than Boolean will cause a
     * {@link javax.validation.ConstraintDeclarationException} upon validation. Any
     * exception occurring during script evaluation will be wrapped into
     * a ConstraintDeclarationException, too. Within the script, the
     * validated object can be accessed from the {@link javax.script.ScriptContext
     * script context} using the name specified in the
     * {@code alias} attribute.
     */
    String script();

    /**
     * @return The name, under which the annotated element shall be registered
     * within the script context. Defaults to "_this".
     */
    String alias() default "_this";

    /**
     * @return The name of the property for which you would like to report a validation error.
     * If given, the resulting constraint violation will be reported on the specified property.
     * If not given, the constraint violation will be reported on the annotated bean.
     * @since 5.4
     */
    String reportOn() default "";

}
