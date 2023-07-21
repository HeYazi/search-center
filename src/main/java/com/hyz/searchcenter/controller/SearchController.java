package com.hyz.searchcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyz.searchcenter.common.BaseResponse;
import com.hyz.searchcenter.common.ErrorCode;
import com.hyz.searchcenter.common.ResultUtils;
import com.hyz.searchcenter.exception.ThrowUtils;
import com.hyz.searchcenter.model.dto.picture.PictureQueryRequest;
import com.hyz.searchcenter.model.dto.post.PostQueryRequest;
import com.hyz.searchcenter.model.dto.search.SearchRequest;
import com.hyz.searchcenter.model.dto.user.UserQueryRequest;
import com.hyz.searchcenter.model.entity.Picture;
import com.hyz.searchcenter.model.vo.PostVO;
import com.hyz.searchcenter.model.vo.SearchVO;
import com.hyz.searchcenter.model.vo.UserVO;
import com.hyz.searchcenter.service.PictureService;
import com.hyz.searchcenter.service.PostService;
import com.hyz.searchcenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.javadoc.PrivateApi;
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
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PictureService pictureService;
    @Resource
    private UserService userService;
    @Resource
    private PostService postService;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);

        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);

        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);

        SearchVO searchVO = new SearchVO();
        searchVO.setUserList(userVOPage.getRecords());
        searchVO.setPostList(postVOPage.getRecords());
        searchVO.setPictureList(picturePage.getRecords());
        return ResultUtils.success(searchVO);
    }


}
