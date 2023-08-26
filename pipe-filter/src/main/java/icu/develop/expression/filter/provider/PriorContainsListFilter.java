package icu.develop.expression.filter.provider;

/**
 * Description:
 * 优先包含
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
public class PriorContainsListFilter extends AbstractPriorMatchListFilter {


    @Override
    protected String filterName() {
        return "prior-contains-list";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.contains(match);
    }
}
