package icu.develop.expression.filter.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/20 10:42
 */
class PipeFilterUtilsTest {

    @Test
    void getPipeFilter() {
        String test = "test|map-filter:key,(max-size:1m)| relpas";
        String[] pipeFilter = PipeFilterUtils.getPipelines(test);
        for (String pipe: pipeFilter) {
            String[] pipeFilter1 = PipeFilterUtils.getPipeFilter(pipe, 2);
            System.out.println(pipeFilter1);
        }
    }
}
