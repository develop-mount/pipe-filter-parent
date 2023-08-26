package icu.develop.expression.filter.provider;

/**
 * Description:
 * 优先equals
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
public class PriorEqualsFilter extends AbstractPriorMatchFilter {


    @Override
    protected String filterName() {
        return "prior-equals";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.equalsIgnoreCase(match);
    }
}
