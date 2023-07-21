package com.hyz.searchcenter.model.dto.search;

import com.hyz.searchcenter.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 聚合搜索请求
 *
 * @author HYZ
 * @date 2023/7/22 0:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -8374954182602992498L;

    /**
     * 搜索文本
     */
    private String searchText;
}
