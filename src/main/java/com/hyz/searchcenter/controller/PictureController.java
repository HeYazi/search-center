package com.hyz.searchcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.hyz.searchcenter.common.BaseResponse;
import com.hyz.searchcenter.common.ErrorCode;
import com.hyz.searchcenter.common.ResultUtils;
import com.hyz.searchcenter.exception.ThrowUtils;
import com.hyz.searchcenter.model.dto.picture.PictureQueryRequest;
import com.hyz.searchcenter.model.entity.Picture;
import com.hyz.searchcenter.model.entity.Post;
import com.hyz.searchcenter.model.vo.PostVO;
import com.hyz.searchcenter.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图片接口
 *
 * @author HYZ
 * @date 2023/07/21
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;


    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest 查询请求后
     * @param request             请求
     * @return {@link BaseResponse}<{@link Page}<{@link PostVO}>>
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                           HttpServletRequest request) {
        // 限制爬虫
        String searchText = pictureQueryRequest.getSearchText();
        long current = pictureQueryRequest.getCurrent();
        long pageSize = pictureQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);

        Page<Picture> pictures = pictureService.searchPicture(searchText, current, pageSize);

        return ResultUtils.success(pictures);
    }
}
