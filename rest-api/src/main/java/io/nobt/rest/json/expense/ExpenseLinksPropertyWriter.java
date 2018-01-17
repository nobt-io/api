package io.nobt.rest.json.expense;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.Annotations;
import io.nobt.core.domain.Expense;
import io.nobt.rest.links.ExpenseLinkFactory;

import java.util.HashMap;

public class ExpenseLinksPropertyWriter extends VirtualBeanPropertyWriter {

    public ExpenseLinksPropertyWriter() {
    }

    public ExpenseLinksPropertyWriter(BeanPropertyDefinition propDef, Annotations contextAnnotations, JavaType declaredType) {
        super(propDef, contextAnnotations, declaredType);
    }

    @Override
    protected Object value(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {

        final ExpenseLinkFactory expenseLinkFactory = (ExpenseLinkFactory) prov.getAttribute(ExpenseLinkFactory.class.getName());

        return new HashMap<String, String>() {{
            put("delete", expenseLinkFactory.createLinkToExpense((Expense) bean).toString());
        }};
    }

    @Override
    public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config, AnnotatedClass declaringClass, BeanPropertyDefinition propDef, JavaType type) {
        return new ExpenseLinksPropertyWriter(propDef, null, type);
    }
}
