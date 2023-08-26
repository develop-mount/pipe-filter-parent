package icu.develop.expression.filter.provider;

/**
 * Description:
 * 优先EndsWith
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/28 15:52
 */
public class PriorEndsWithFilter extends AbstractPriorMatchFilter {

    @Override
    protected String filterName() {
        return "prior-ends-with";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.endsWith(match);
    }
}
