package com.zhupeng.baseframe.entity.common;

import com.zhupeng.baseframe.entity.ro.BaseCommonResponse;
import io.swagger.annotations.ApiModelProperty;

/**
 * 公共查询列表返回结果
 *
 * @author zhupeng
 *
 */
public class CommonResponse<T> extends BaseCommonResponse {

    private static final long serialVersionUID = 1L;
    //集合数据
    @ApiModelProperty(value="集合数据,具体内容会更具实际情况而改变")
    private T data ;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}