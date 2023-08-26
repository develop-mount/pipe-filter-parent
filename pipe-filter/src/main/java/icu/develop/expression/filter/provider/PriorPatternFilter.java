package icu.develop.expression.filter.provider;

import java.util.regex.Pattern;

/**
 * Description:
 * 优先模式匹配
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/29 14:48
 */
public class PriorPatternFilter extends AbstractPriorMatchFilter {
    @Override
    protected String filterName() {
        return "prior-pattern";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return Pattern.matches(match, source);
    }
}
