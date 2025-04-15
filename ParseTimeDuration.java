
package directives;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.annotations.*;
import java.util.*;
import java.util.regex.*;

@Plugin(type = Directive.TYPE)
@Name("parse-duration")
@Description("Parses time durations like '5h', '2d' to seconds.")
public class ParseTimeDuration implements Directive {
    private String column;

    @Override
    public void initialize(DirectiveContext context) {
        this.column = context.getProperties().get("column");
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        Map<String, Long> timeUnits = Map.of(
            "s", 1L,
            "m", 60L,
            "h", 3600L,
            "d", 86400L
        );

        Pattern pattern = Pattern.compile("(\d+)([smhd])");

        for (Row row : rows) {
            String raw = row.getValue(column).toString();
            Matcher matcher = pattern.matcher(raw);
            if (matcher.matches()) {
                long val = Long.parseLong(matcher.group(1));
                String unit = matcher.group(2).toLowerCase();
                long seconds = val * timeUnits.getOrDefault(unit, 1L);
                row.setValue(column, seconds);
            }
        }

        return rows;
    }

    @Override
    public void destroy() { }
}
