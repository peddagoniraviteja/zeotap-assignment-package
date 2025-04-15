
package wrangler.directives;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.annotations.*;

@Plugin(type = Directive.TYPE)
@Name("example-directive")
@Description("This is a placeholder directive for assignment 1.")
public class ExampleDirective implements Directive {
    @Override
    public void initialize(DirectiveContext context) {}

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        return rows;
    }

    @Override
    public void destroy() {}
}
