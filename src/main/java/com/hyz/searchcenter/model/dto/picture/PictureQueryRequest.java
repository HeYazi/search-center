package com.hyz.searchcenter.model.dto.picture;

import com.hyz.searchcenter.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 图片 DTO
 *
 * @author HYZ
 * @date 2023/7/21 10:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PictureQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 7068268455310270070L;
    /**
     * 搜索文本
     */
    private String searchText;
}
