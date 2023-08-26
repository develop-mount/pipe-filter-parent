package icu.develop.expression.filter.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/7/20 14:07
 */
class VariableUtilsTest {

    @Test
    void split1() {
        String[] split = PipeFilterUtils.split(" ( max-size:12,123 ), 18", ",");
        System.out.println(split);
    }

    @Test
    void split2() {
        String[] split = PipeFilterUtils.split("12, (max-size:12,123),18", ",");
        System.out.println(Arrays.asList(split));
    }

    @Test
    void split3() {
        String[] split = PipeFilterUtils.split("12, (max-size:12,123),(max-size:12,123)", ",");
        System.out.println(Arrays.asList(split));
    }

    @Test
    void test4() {
        String[] split = PipeFilterUtils.split("12, (,),(max-size:12,123)", ",");
        System.out.println(Arrays.asList(split));
    }

    @Test
    void test5() {
        String[] split = PipeFilterUtils.split("12, (,),(max-size:12,123", ",");
        System.out.println(Arrays.asList(split));
    }

    @Test
    void test6() {
        String[] split = PipeFilterUtils.split("123, (max-size:12)", ",");
        System.out.println(Arrays.asList(split));
    }

    @Test
    void test8() {
        String[] split = PipeFilterUtils.split("123,tets,", ",");
        System.out.println(Arrays.asList(split));
    }

    @Test
    void test9() {
        String source = "(tt1:22), (tt:test), test11 ";
        String[] split = PipeFilterUtils.split(source, ",");
        System.out.println(Arrays.asList(split));

        String[] split1 = source.split(",");
        System.out.println(Arrays.asList(split1));
    }

    @Test
    void test10() {
        String source = "(|), (tt:test)";
        String[] split = PipeFilterUtils.split(source, ",");
        System.out.println(Arrays.asList(split));
    }

}
