package icu.develop.expression.filter.provider;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * ends-with
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
@Slf4j
public class ContainsListFilter extends AbstractMatchListFilter {

    @Override
    protected String filterName() {
        return "contains-list";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.contains(match);
    }

}
