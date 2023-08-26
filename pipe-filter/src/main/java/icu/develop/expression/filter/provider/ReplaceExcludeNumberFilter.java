package icu.develop.expression.filter.provider;

import icu.develop.expression.filter.BasePipeFilter;
import icu.develop.expression.filter.PipeDataWrapper;
import icu.develop.expression.filter.constant.EscapeEnums;
import icu.develop.expression.filter.utils.PipeFilterUtils;
import icu.develop.expression.filter.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/6/17 11:40
 */
@Slf4j
public class ReplaceExcludeNumberFilter extends BasePipeFilter<Object, Object> {

    private static final int PARAMS_NUM = 3;

    @Override
    protected String filterName() {
        return "replace-exclude-number";
    }

    @Override
    public PipeDataWrapper<Object> handlerApply(PipeDataWrapper<Object> wrapper) {

        // 验证
        if (!verify(wrapper)) {
            return wrapper;
        }

        Object value = wrapper.getData();
        if (Objects.isNull(value)) {
            return PipeDataWrapper.error(errorPrefix() + "传入数据不能为空");
        }

        if (PipeFilterUtils.isEmpty(params()) || params().size() != PARAMS_NUM) {
            return PipeDataWrapper.error(errorPrefix() + "[replace-exclude-number:oldChar,newChar,all]传入参数下标为空或是超过两个");
        }

        String oldChar = params().get(0);
        if (StringUtils.isBlank(oldChar)) {
            return PipeDataWrapper.error(errorPrefix() + "[replace-exclude-number:oldChar,newChar,all]中传入参数oldChar为空");
        }

        String newChar = params().get(1);
        if (StringUtils.isBlank(newChar)) {
            return PipeDataWrapper.error(errorPrefix() + "[replace-exclude-number:oldChar,newChar,all]中传入参数newChar为空");
        }

        String all = params().get(2);
        if (StringUtils.isBlank(all)) {
            return PipeDataWrapper.error(errorPrefix() + "[replace-exclude-number:oldChar,newChar,all]中传入参数all为空,不是1/0");
        }
        if (!StringUtils.isNumeric(all)) {
            return PipeDataWrapper.error(errorPrefix() + "[replace-exclude-number:oldChar,newChar,all]中传入参数all错误,不是1/0");
        }

        if (!(value instanceof String)) {
            return PipeDataWrapper.error(errorPrefix() + "指令输入数据不是字符串");
        }

        try {
            String oldCharEs = oldChar;
            EscapeEnums oldCharEscapeEnums = EscapeEnums.match(oldChar);
            if (Objects.nonNull(oldCharEscapeEnums)) {
                oldCharEs = oldCharEscapeEnums.getCh();
            }

            String newCharEs = newChar;
            EscapeEnums newCharEscapeEnums = EscapeEnums.match(newChar);
            if (Objects.nonNull(newCharEscapeEnums)) {
                newCharEs = newCharEscapeEnums.getCh();
            }

            int allInt = Integer.parseInt(all);
            if (1 == allInt) {
                return PipeDataWrapper.success(replaceExcludeNumber((String) value, oldCharEs, newCharEs));
            } else {
                return PipeDataWrapper.success(replaceExcludeNumber((String) value, oldCharEs, newCharEs));
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return PipeDataWrapper.error(errorPrefix() + "[replace-exclude-number:oldChar,newChar,all]中传入参数all转换数字错误");
        }
    }

    /**
     * 方法描述:替换数字之外的所有 老字符串为新字符串
     * @param str 待处理字符串
     * @param oldStr 带替换字符串
     * @param newStr 要替换字符串
     * @return java.lang.String
     * @since  2023/7/27 13:57
     */
    public static String replaceExcludeNumber(String str,String oldStr,String newStr){
        StringBuilder sb = new StringBuilder();
        while (StringUtils.isNotBlank(str) && str.contains(oldStr)){
            String preStr = str.substring(0,str.indexOf(oldStr));
            String nextStr = str.substring(str.indexOf(oldStr));
            //以oldStr 结尾并且只剩下关键字
            if(oldStr.equals(nextStr)){
                sb.append(preStr).append(newStr);
                str = "";
            }
            //以oldStr开头
            else if(str.indexOf(oldStr) == 0){
                str = str.substring(str.indexOf(oldStr)+1);
                sb.append(newStr);
            }else{
                //判断要替换的关键词前后是否为数字
                char preChar = str.charAt(str.indexOf(oldStr)-1);
                char nexChar = str.charAt(str.indexOf(oldStr)+1);
                sb.append(preStr);
                //均为数字则无需替换
                if(Character.isDigit(preChar) && Character.isDigit(nexChar)){
                    sb.append(oldStr);
                }
                //否则
                else{
                    sb.append(newStr);
                }
                str = str.substring(str.indexOf(oldStr)+1);
            }
        }
        //将不包含要替换的关键词的 剩余部分添加到后面。
        sb.append(str);
        return sb.toString();
    }
}
