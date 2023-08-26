package icu.develop.expression.filter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Description:
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/29 10:13
 */
public interface PipeFilter<T, R> extends Function<PipeDataWrapper<T>, PipeDataWrapper<R>> {

    /**
     * 执行具体方法
     *
     * @param wrapper the function argument
     * @return
     */
    @Override
    PipeDataWrapper<R> apply(PipeDataWrapper<T> wrapper);

    /**
     * filter 参数集合
     *
     * @return 参数集合
     */
    List<String> params();

    /**
     * 添加参数到pipe filter
     *
     * @param params 参数
     * @return pipe filter
     */
    default PipeFilter<T, R> addParams(String... params) {
        params().addAll(Arrays.asList(params));
        return this;
    }

    /**
     * 额外附加信息
     * @return 附加信息
     */
    List<Object> extra();

    /**
     * 添加额外信息
     * @param extra 附加信息
     * @return pipe filter
     */
    default PipeFilter<T, R> addExtra(Object... extra) {
        extra().addAll(Arrays.asList(extra));
        return this;
    }
}
