package com.zhupeng.baseframe.entity.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 使用Pagehelper 的查询抽象类
 * @author zhupeng
 *
 */
@ApiModel(value="分页用参数实体类")
public class AbstractPagehelperRequest {

    /**
     * 当前页
     */
    @ApiModelProperty(value="当前页")
    private Integer currentPage = 1 ;
    /**
     * 当前页数据量
     */
    @ApiModelProperty(value="当前页数据量")
    private Integer limit = 10;
    /**
     * 是否分页
     */
    @ApiModelProperty(value="是否分页")
    private Boolean isPaging = true;
    /**
     * 按照哪那个字段排序
     */
    @ApiModelProperty(value="按照哪那个字段排序")
    private String orderBy = "";
    /**
     * 排序规则：降序：" desc"，升序" asc"
     */
    @ApiModelProperty(value="排序规则：降序：\" desc\"，升序\" asc\"")
    private String sort = " asc";

    public void setOrderByDesc(String orderBy)
    {
        this.orderBy = orderBy;
        this.sort = " desc";
    }

    public void setOrderByAsc(String orderBy)
    {
        this.orderBy = orderBy;
        this.sort = " asc";
    }

    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public Integer getLimit() {
        return limit;
    }
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    public Boolean getIsPaging() {
        return isPaging;
    }
    public void setIsPaging(Boolean isPaging) {
        this.isPaging = isPaging;
    }
    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }

}
