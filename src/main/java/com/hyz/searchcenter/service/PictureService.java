package com.hyz.searchcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyz.searchcenter.model.entity.Picture;

import java.util.List;

/**
 * 图片服务
 *
 * @author HYZ
 * @date 2023/07/21
 */
public interface PictureService {
    /**
     * 搜索图片
     *
     * @param searchText 搜索文本
     * @param pageNum    页数
     * @param pageSize   页面大小
     * @return {@link List}<{@link Picture}>
     */
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
