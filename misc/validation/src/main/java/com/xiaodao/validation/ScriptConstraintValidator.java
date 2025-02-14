package com.xiaodao.validation;

import cn.hutool.core.lang.Pair;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorInitializationContext;
import org.hibernate.validator.internal.constraintvalidators.hv.AbstractScriptAssertValidator;
import org.hibernate.validator.internal.engine.messageinterpolation.util.InterpolationHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.spi.scripting.ScriptEvaluationException;
import org.hibernate.validator.spi.scripting.ScriptEvaluator;
import org.hibernate.validator.spi.scripting.ScriptEvaluatorNotFoundException;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidatorContext;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.invoke.MethodHandles;
import java.util.Map;

import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;
import static org.hibernate.validator.internal.util.logging.Messages.MESSAGES;

@Slf4j
public class ScriptConstraintValidator extends AbstractScriptAssertValidator<ScriptConstraint, Object> {

    private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

    private String alias;
    private String reportOn;
    private String message;

    protected ScriptAssertContext scriptAssertContext;


    @Override
    public void initialize(ConstraintDescriptor<ScriptConstraint> constraintDescriptor, HibernateConstraintValidatorInitializationContext initializationContext) {
        ScriptConstraint constraintAnnotation = constraintDescriptor.getAnnotation();

        validateParameters(constraintAnnotation);
        // initialize(constraintAnnotation.lang(), constraintAnnotation.script(), initializationContext);

        this.script = constraintAnnotation.script();
        this.languageName = constraintAnnotation.lang();
        this.escapedScript = InterpolationHelper.escapeMessageParameter(script);

        try {
            ScriptEvaluator scriptEvaluator = initializationContext.getScriptEvaluatorForLanguage(languageName);
            scriptAssertContext = new ScriptAssertContext(script, scriptEvaluator);
        } catch (ScriptEvaluatorNotFoundException e) {
            throw LOG.getCreationOfScriptExecutorFailedException(languageName, e);
        }

        this.alias = constraintAnnotation.alias();
        this.reportOn = constraintAnnotation.reportOn();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
            constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).addMessageParameter("script", escapedScript);
        }

        Pair<Boolean, String> validationResult = scriptAssertContext.evaluateScriptAssertExpression(value, alias);

        if (!validationResult.getKey()) {
            if (!reportOn.isEmpty() || validationResult.getValue() != null) {
                String lastMessage = validationResult.getValue() != null ? validationResult.getValue() : message;
                constraintValidatorContext.disableDefaultConstraintViolation();
                final ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder =
                        constraintValidatorContext.buildConstraintViolationWithTemplate(lastMessage);
                if (!reportOn.isEmpty()) {
                    constraintViolationBuilder.addPropertyNode(reportOn);
                }
                constraintViolationBuilder.addConstraintViolation();
            }
        }

        return validationResult.getKey();
    }

    private void validateParameters(ScriptConstraint constraintAnnotation) {
        Contracts.assertNotEmpty(constraintAnnotation.script(), MESSAGES.parameterMustNotBeEmpty("script"));
        Contracts.assertNotEmpty(constraintAnnotation.lang(), MESSAGES.parameterMustNotBeEmpty("lang"));
        Contracts.assertNotEmpty(constraintAnnotation.alias(), MESSAGES.parameterMustNotBeEmpty("alias"));
    }

    protected static class ScriptAssertContext {

        private static final Log LOG = LoggerFactory.make(MethodHandles.lookup());

        private final String script;
        private final ScriptEvaluator scriptEvaluator;

        public ScriptAssertContext(String script, ScriptEvaluator scriptEvaluator) {
            this.script = script;
            this.scriptEvaluator = scriptEvaluator;
        }

        public Pair<Boolean, String> evaluateScriptAssertExpression(Object object, String alias) {
            Map<String, Object> bindings = newHashMap();
            bindings.put(alias, object);

            return evaluateScriptAssertExpression(bindings);
        }

        public Pair<Boolean, String> evaluateScriptAssertExpression(Map<String, Object> bindings) {
            Object result;

            try {
                result = scriptEvaluator.evaluate(script, bindings);
            } catch (ScriptEvaluationException e) {
                throw LOG.getErrorDuringScriptExecutionException(script, e);
            }

            return handleResult(result);
        }

        private Pair<Boolean, String> handleResult(Object evaluationResult) {
            if (evaluationResult == null) {
                throw LOG.getScriptMustReturnTrueOrFalseException(script);
            }

            if (!(evaluationResult instanceof Boolean)) {
                if (evaluationResult instanceof String) {
                    return Pair.of(Boolean.FALSE, evaluationResult.toString());
                } else {
                    throw new ConstraintDeclarationException(String.format("HV000025: Script \"%1$s\" returned %2$s (of type %3$s), but must return either true or false or string.",
                            script, evaluationResult, evaluationResult.getClass().getCanonicalName()));
                }
            }

            return Pair.of(Boolean.TRUE.equals(evaluationResult), null);
        }
    }
}
