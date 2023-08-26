package icu.develop.expression.filter.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/20 17:57
 */
public class DateTest {

    @Test
    void testDate() {
        DateTime parse = DateUtil.parse("2023-04-01");
        DateTime parse1 = DateUtil.parse("2023-04-01 00:00");
        DateTime parse2 = DateUtil.parse("2023-04-01 00:00:01");
        DateTime parse3 = DateUtil.parse("2023/04/01 00:00:01");
        DateTime parse4 = DateUtil.parse("2023/04/01 00:00");
        DateTime parse5 = DateUtil.parse("2023/04/01");
        DateTime parse6 = DateUtil.parse("2023123");
        System.out.println(parse5);
    }
}
