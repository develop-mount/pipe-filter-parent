package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.RandomStringUtils;
import icu.develop.expression.filter.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * must pipe filter
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/26 8:34
 */
@Slf4j
public class RandomStringFilter extends BasePipeFilter<Object, Object> {

    public static final int RANDOM_MAX = 512;

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        if (PipeFilterUtils.isEmpty(params())) {
            return PipeDataWrapper.error(errorPrefix() + "参数为空");
        }
        if (params().size() != 2) {
            return PipeDataWrapper.error(errorPrefix() + "仅支持2个参数");
        }

        String mode = params().get(0);
        String digit = params().get(1);
        if (StringUtils.isBlank(mode) || StringUtils.isBlank(digit)) {
            return PipeDataWrapper.error(errorPrefix() + "模式及位数不能为空");
        }
        if (!ModeEnums.isMode(mode)) {
            return PipeDataWrapper.error(errorPrefix() + "mode模式参数格式不正确");
        }

        int digitInt = 12;
        if (StringUtils.isNumeric(digit)) {
            digitInt = convert(digit, 6);
        }
        if (digitInt > RANDOM_MAX) {
            return PipeDataWrapper.error(errorPrefix() + "digit位数最大值为512");
        }

        if (ModeEnums.isNumeric(mode)) {
            return PipeDataWrapper.success(RandomStringUtils.randomNumeric(digitInt));
        }
        if (ModeEnums.isAlphabetic(mode)) {
            return PipeDataWrapper.success(RandomStringUtils.randomAlphabetic(digitInt));
        }
        if (ModeEnums.isAlphanumeric(mode)) {
            return PipeDataWrapper.success(RandomStringUtils.randomAlphanumeric(digitInt));
        }

        return PipeDataWrapper.success(RandomStringUtils.randomNumeric(digitInt));
    }

    @Override
    protected String filterName() {
        return "random-string";
    }


    public static int convert(String mode, int defaultInt) {
        try {
            return Integer.parseInt(mode);
        } catch (Exception e) {
            log.warn("格式转换错误：{}", e.getMessage());
        }
        return defaultInt;
    }

    public enum ModeEnums {
        // 数字
        ONE("1"),
        // 大小写字母
        TWO("2"),
        // 数字及大小写字母
        TREE("3");
        private final String value;
        ModeEnums(String value) {
            this.value = value;
        }

        /**
         * 是字母及数字
         * @param mode 模式
         * @return true or false
         */
        public static boolean isAlphanumeric(String mode) {
            return TREE.value.equals(mode);
        }
        /**
         * 是字母
         * @param mode 模式
         * @return true or false
         */
        public static boolean isAlphabetic(String mode) {
            return TWO.value.equals(mode);
        }
        /**
         * 是数字
         * @param mode 模式
         * @return true or false
         */
        public static boolean isNumeric(String mode) {
            return ONE.value.equals(mode);
        }

        public static boolean isMode(String mode) {
            for (ModeEnums modeEnums : ModeEnums.values()) {
                if (modeEnums.value.equals(mode)) {
                    return true;
                }
            }
            return false;
        }
    }
}
