package icu.develop.expression.filter.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/8/1 18:02
 */
class RandomStringUtilsTest {

    @Test
    void random() {
        String random = RandomStringUtils.randomAlphanumeric(16);
        System.out.println(random);
    }
}
