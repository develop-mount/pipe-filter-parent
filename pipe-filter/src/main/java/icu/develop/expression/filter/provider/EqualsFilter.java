package icu.develop.expression.filter.provider;

/**
 * Description:
 * equals
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
public class EqualsFilter extends AbstractMatchFilter {

    @Override
    protected String filterName() {
        return "equals";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.equalsIgnoreCase(match);
    }
}
