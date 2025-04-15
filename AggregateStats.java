
package directives;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.annotations.*;
import java.util.*;

@Plugin(type = Directive.TYPE)
@Name("aggregate-stats")
@Description("Computes count, min, max, avg, and sum for a numeric column.")
public class AggregateStats implements Directive {
    private String column;

    @Override
    public void initialize(DirectiveContext context) throws DirectiveInitializeException {
        this.column = context.getProperties().get("column");
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecuteException {
        List<Double> values = new ArrayList<>();
        for (Row row : rows) {
            Object val = row.getValue(column);
            if (val instanceof Number) {
                values.add(((Number) val).doubleValue());
            }
        }

        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        double avg = sum / values.size();
        double min = values.stream().min(Double::compare).orElse(0.0);
        double max = values.stream().max(Double::compare).orElse(0.0);

        Row result = new Row();
        result.add("count", values.size());
        result.add("sum", sum);
        result.add("avg", avg);
        result.add("min", min);
        result.add("max", max);

        return Collections.singletonList(result);
    }

    @Override
    public void destroy() { }
}
