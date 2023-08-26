package icu.develop.expression.filter.provider;

/**
 * Description:
 * 优先startWith
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/28 15:52
 */
public class PriorStartsWithListFilter extends AbstractPriorMatchListFilter {

    @Override
    protected String filterName() {
        return "starts-with-list";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.startsWith(match);
    }
}
