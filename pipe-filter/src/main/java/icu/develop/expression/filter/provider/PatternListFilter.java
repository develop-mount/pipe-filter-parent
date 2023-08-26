package icu.develop.expression.filter.provider;

import java.util.regex.Pattern;

/**
 * Description:
 * pattern-list
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/30 10:37
 */
public class PatternListFilter extends AbstractMatchListFilter {

    @Override
    protected String filterName() {
        return "pattern-list";
    }

    @Override
    protected boolean matchProcess(String source, String match) {
        return Pattern.matches(match, source);
    }
}
