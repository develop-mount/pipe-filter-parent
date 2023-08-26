package icu.develop.expression.filter.provider;

/**
 * Description:
 * 包含
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
public class ContainsFilter extends AbstractMatchFilter {

    @Override
    protected String filterName() {
        return "contains";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.contains(match);
    }
}
