package multimodalinterviewaiback.service.impl;

import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.mapper.UserMapper;
import multimodalinterviewaiback.pojo.dto.GetUserInfoDTO;
import multimodalinterviewaiback.pojo.entity.Result;
import multimodalinterviewaiback.pojo.entity.User;
import multimodalinterviewaiback.pojo.entity.UserEntity;
import multimodalinterviewaiback.pojo.vo.login.ReturnGetUserInfo;
import multimodalinterviewaiback.pojo.vo.user.PostUpdatePasswordVO;
import multimodalinterviewaiback.pojo.vo.user.PostUpdateProtectQuestionVO;
import multimodalinterviewaiback.service.UserService;
import multimodalinterviewaiback.tools.AliOssTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Component
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AliOssTool aliOssTool;

    @Override
    public int findUserCountByUsername(String username) {
        return userMapper.findUserCountByUsername(username);
    }

    @Override
    public String getUserPassword(String username) {
        return userMapper.getUserPassword(username);
    }

    @Override
    public void addUser(User a) {
        userMapper.addUser(a);
    }

    @Override
    public String getUserPictureUrl(String username) {
        return userMapper.getPictureUrlByUsername(username);
    }


    @Override
    public String updateUserPicture(String username, MultipartFile file) {
        try{
            // 删除原图片
            String oldPictureUrl = userMapper.getPictureUrlByUsername(username);
            if(oldPictureUrl != null && !oldPictureUrl.isEmpty()){
                aliOssTool.deleteFile(oldPictureUrl);
            }
            // 获取当前图片扩展名
            String originalFileName = file.getOriginalFilename();
            int index = 0;
            if (originalFileName != null) {
                index = originalFileName.lastIndexOf(".");
            }
            String extname = originalFileName.substring(index);
            // 生成新的文件名（唯一）
            String newFileName = UUID.randomUUID() + extname;
            String pictureUrl = aliOssTool.upload(file.getBytes(), newFileName);
            int flag = userMapper.updatePictureUrl(username, pictureUrl);
            if(flag > 0){
                return pictureUrl;
            }
            else {
                return null;
            }
        }catch (Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public ReturnGetUserInfo getUserInfo(String username) {
        try{
            ReturnGetUserInfo returnGetUserInfo = new ReturnGetUserInfo();
            returnGetUserInfo.setUsername(username);
            GetUserInfoDTO getUserInfoDTO = userMapper.getUserInfo(username);
            returnGetUserInfo.setName(getUserInfoDTO.getName());
            returnGetUserInfo.setEmail(getUserInfoDTO.getEmail());
            returnGetUserInfo.setSex(getUserInfoDTO.getSex());
            returnGetUserInfo.setPhone(getUserInfoDTO.getPhone());
            returnGetUserInfo.setMajor(getUserInfoDTO.getMajor());
            returnGetUserInfo.setInterviewCount(getUserInfoDTO.getInterviewCount());
            returnGetUserInfo.setComprehensiveScore(getUserInfoDTO.getComprehensiveScore());
            returnGetUserInfo.setPictureUrl(getUserInfoDTO.getPictureUrl());

            return returnGetUserInfo;
        }catch (Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean updatePassword(String username, PostUpdatePasswordVO postUpdatePasswordVO) {
        // 获取旧密码
        String oldPassword = userMapper.findUserPassword(username);
        // 验证旧密码是否正确
        if (!oldPassword.equals(postUpdatePasswordVO.getOldPassword())) {
            return false;
        }
        // 修改密码
        int flag = userMapper.updatePassword(username, postUpdatePasswordVO.getNewPassword());
        return flag > 0;
    }

    @Override
    public boolean updateUserProtectQuestion(String username, PostUpdateProtectQuestionVO postUpdateProtectQuestion) {
        // 获取旧密码
        String oldPassword = userMapper.findUserPassword(username);
        // 验证旧密码是否正确
        if (!oldPassword.equals(postUpdateProtectQuestion.getPassword())) {
            return false;
        }
        // 修改密保问题、答案
        int flag = userMapper.updateUserProtectQuestion(username, postUpdateProtectQuestion);
        return flag > 0;
    }


    public boolean updateUserInfo(UserEntity updateDTO) {

        // 1. 查询用户是否存在
        UserEntity user = userMapper.selectById(userMapper.getUserIdByUsername(updateDTO.getUsername()));
        if (user == null) {
            return false;
        }

        user.setName(updateDTO.getName());
        user.setEmail(updateDTO.getEmail());
        user.setMajor(updateDTO.getMajor());
        user.setPhone(updateDTO.getPhone());
        user.setPassword(updateDTO.getPassword());
        user.setComprehensiveScore(updateDTO.getComprehensiveScore());
        user.setPictureUrl(updateDTO.getPictureUrl());
        user.setInterviewCount(updateDTO.getInterviewCount());
        user.setSex(updateDTO.getSex());

        // 3. 执行数据库更新
        int rows = userMapper.updateUserInfo(user);
        if (rows > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Long getUserId(String username, String password) {
        return userMapper.selectId(username, password);
    }
}
