package com.hyz.searchcenter.model.vo;

import com.hyz.searchcenter.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索 VO
 *
 * @author HYZ
 * @date 2023/7/22 0:57
 */
@Data
public class SearchVO implements Serializable {

    private static final long serialVersionUID = -3050504178174520385L;

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;

}
