package icu.develop.expression.filter.provider;

import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * 优先EndsWith
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/28 15:52
 */
@Slf4j
public class PriorEndsWithListFilter extends AbstractPriorMatchListFilter {

    @Override
    protected String filterName() {
        return "prior-ends-with-list";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return source.endsWith(match);
    }

}
