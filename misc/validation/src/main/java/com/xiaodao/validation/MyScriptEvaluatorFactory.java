package com.xiaodao.validation;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.hibernate.validator.internal.engine.scripting.DefaultScriptEvaluatorFactory;
import org.hibernate.validator.spi.scripting.ScriptEvaluationException;
import org.hibernate.validator.spi.scripting.ScriptEvaluator;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

public class MyScriptEvaluatorFactory extends DefaultScriptEvaluatorFactory {

    public MyScriptEvaluatorFactory(ClassLoader externalClassLoader) {
        super(externalClassLoader);
    }

    @Override
    public ScriptEvaluator createNewScriptEvaluator(String languageName) {
        if ("spring".equalsIgnoreCase(languageName)) {
            return new SpringELScriptEvaluator();
        }
        if ("qlExpress".equalsIgnoreCase(languageName)) {
            return new QLExpressELScriptEvaluator();
        }
        return super.createNewScriptEvaluator(languageName);
    }

    private static class SpringELScriptEvaluator implements ScriptEvaluator {

        private final ExpressionParser expressionParser = new SpelExpressionParser();

        @Override
        public Object evaluate(String script, Map<String, Object> bindings) throws ScriptEvaluationException {
            try {
                Expression expression = expressionParser.parseExpression(script);
                EvaluationContext context = new StandardEvaluationContext(bindings.values().iterator().next());
                for (Map.Entry<String, Object> binding : bindings.entrySet()) {
                    context.setVariable(binding.getKey(), binding.getValue());
                }
                return expression.getValue(context);
            } catch (ParseException | EvaluationException e) {
                throw new ScriptEvaluationException("Unable to evaluate SpEL script", e);
            }
        }
    }

    /**
     * 使用 QLExpress 实现脚本验证
     */
    private static class QLExpressELScriptEvaluator implements ScriptEvaluator {
        private final ExpressRunner runner = new ExpressRunner();


        @Override
        public Object evaluate(String script, Map<String, Object> bindings) throws ScriptEvaluationException {
            DefaultContext<String, Object> context = new DefaultContext<>();
            context.putAll(bindings);

            try {
                return runner.execute(script, context, null, true, false);
            } catch (Exception e) {
                throw new ScriptEvaluationException("Unable to evaluate QLExpress script", e);
            }
        }
    }
}