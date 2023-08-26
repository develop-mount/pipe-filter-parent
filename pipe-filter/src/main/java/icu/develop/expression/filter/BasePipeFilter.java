package icu.develop.expression.filter;

import java.util.*;

/**
 * Description:
 * pipeline filter
 *
 * @author linfeng
 * @version 1.0.0
 * @since 2023/5/27 21:58
 */
public abstract class BasePipeFilter<T, R> implements PipeFilter<T, R> {

    private final List<String> filterParams = new ArrayList<>();
    private final List<Object> extraList = new ArrayList<>();

    protected String noticePrefix;

    /**
     * filter名称
     *
     * @return 名称
     */
    protected abstract String filterName();

    protected boolean isValidity(PipeDataWrapper<R> apply) {
        Object data = apply.getData();
        if (Objects.nonNull(data)) {
            return !(data instanceof Collection || data instanceof Map);
        }
        return true;
    }

    public void setNoticePrefix(String noticePrefix) {
        this.noticePrefix = noticePrefix;
    }

    /**
     * @return 错误信息前缀
     */
    protected String errorPrefix() {
        return noticePrefix + String.format("[%s]指令错误:", filterName());
    }

    /**
     * filter 参数
     *
     * @return 参数集合
     */
    public List<String> params() {
        return filterParams;
    }

    /**
     * 添加参数
     *
     * @param params 参数
     * @return 过滤器
     */
    public BasePipeFilter<T, R> addParams(String... params) {
        params().addAll(Arrays.asList(params));
        return this;
    }

    @Override
    public List<Object> extra() {
        return extraList;
    }

    @Override
    public PipeFilter<T, R> addExtra(Object... extra) {
        extra().addAll(Arrays.asList(extra));
        return this;
    }

    /**
     * 验证
     *
     * @param wrapper 通道数据包装
     * @return 是否成功
     */
    protected boolean verify(PipeDataWrapper<T> wrapper) {
        return Objects.nonNull(wrapper) && (wrapper.isErrorContinue() || wrapper.success());
    }

    /**
     * 处理指令逻辑
     * @param wrapper wrapper
     * @return 包裹对象
     */
    protected PipeDataWrapper<R> handlerApply(PipeDataWrapper<T> wrapper) {
        return null;
    }

    @Override
    public PipeDataWrapper<R> apply(PipeDataWrapper<T> wrapper) {
        // 设置错误忽略
        PipeDataWrapper<R> dataWrapper = handlerApply(wrapper).errorContinue(wrapper.isErrorContinue());
        // 若前面指令设置错误继续执行，且前面指令执行错误，则设置错误状态透传到下一指令
        if (wrapper.isErrorContinue() && !wrapper.success()) {
            dataWrapper.setMessage(wrapper.getMessage());
            dataWrapper.setStatus(wrapper.getStatus());
        }
        return dataWrapper;
    }
}
