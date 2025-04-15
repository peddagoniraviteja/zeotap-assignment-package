
package directives;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.annotations.*;
import java.util.*;
import java.util.regex.*;

@Plugin(type = Directive.TYPE)
@Name("parse-bytesize")
@Description("Parses a string like '10MB' to its size in bytes.")
public class ParseByteSize implements Directive {
    private String column;

    @Override
    public void initialize(DirectiveContext context) {
        this.column = context.getProperties().get("column");
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        Map<String, Long> unitMap = Map.of(
            "B", 1L,
            "KB", 1024L,
            "MB", 1024L * 1024,
            "GB", 1024L * 1024 * 1024
        );

        Pattern pattern = Pattern.compile("(\d+(\.\d+)?)([A-Za-z]+)");

        for (Row row : rows) {
            String raw = row.getValue(column).toString();
            Matcher matcher = pattern.matcher(raw);
            if (matcher.matches()) {
                double value = Double.parseDouble(matcher.group(1));
                String unit = matcher.group(3).toUpperCase();
                long bytes = (long) (value * unitMap.getOrDefault(unit, 1L));
                row.setValue(column, bytes);
            }
        }
        return rows;
    }

    @Override
    public void destroy() { }
}
