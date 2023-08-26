package icu.develop.expression.filter.provider;

/**
 * Description:
 * 优先startWith
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/28 15:52
 */
public class PriorStartsWithFilter extends AbstractPriorMatchFilter {

    @Override
    protected String filterName() {
        return "starts-with";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.startsWith(match);
    }
}
