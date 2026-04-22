package multimodalinterviewaiback.controller;


import multimodalinterviewaiback.mapper.UserMapper;
import multimodalinterviewaiback.pojo.entity.Result;
import lombok.extern.slf4j.Slf4j;
import multimodalinterviewaiback.common.BaseContext;
import multimodalinterviewaiback.pojo.entity.UserEntity;
import multimodalinterviewaiback.pojo.vo.login.ReturnGetUserInfo;
import multimodalinterviewaiback.pojo.vo.user.PostUpdatePasswordVO;
import multimodalinterviewaiback.pojo.vo.user.PostUpdateProtectQuestionVO;
import multimodalinterviewaiback.pojo.vo.user.PostUpdateUserPictureVO;
import multimodalinterviewaiback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@ResponseBody // 返回JSON
@CrossOrigin("*") // 允许跨域
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/getUserInfo")
    public Result getUserInfo(){
        log.info("获取用户基本信息,当前用户：{}", BaseContext.getCurrentUsername());
        try{
            ReturnGetUserInfo returnGetUserInfo = userService.getUserInfo(BaseContext.getCurrentUsername());
            log.info("获取成功：{}", returnGetUserInfo);

            return Result.success(returnGetUserInfo);
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error("获取信息失败");
        }
    }

    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody ReturnGetUserInfo updateDTO){
        log.info("修改用户信息");
        try {
            UserEntity user = new UserEntity();
            user.setUsername(updateDTO.getUsername());
            user.setName(updateDTO.getName());
            user.setEmail(updateDTO.getEmail());
            user.setMajor(updateDTO.getMajor());
            user.setPhone(updateDTO.getPhone());
            user.setComprehensiveScore(updateDTO.getComprehensiveScore());
            user.setPictureUrl(updateDTO.getPictureUrl());
            user.setInterviewCount(updateDTO.getInterviewCount());
            user.setSex(updateDTO.getSex());

            if(userService.updateUserInfo(user)){
                return Result.success("更新成功",null);
            }else {
                return Result.error("更新失败");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }


    @PostMapping("/updateUserPicture")
    public Result updateUserPicture(@RequestParam MultipartFile picture){
        log.info("修改用户头像");
        try{
            String result = userService.updateUserPicture(BaseContext.getCurrentUsername(), picture);
            if (result != null) {
                return Result.success("个人头像修改成功",result);
            } else {
                return Result.error("个人头像修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    public Result updateUserPassword(@RequestBody PostUpdatePasswordVO postUpdatePasswordVO) {
        log.info("修改密码");
        try{
            boolean result = userService.updatePassword(postUpdatePasswordVO.getUsername(), postUpdatePasswordVO);
            if (result) {
                log.info("密码修改成功");
                return Result.success("密码修改成功",null);
            } else {
                return Result.error("密码修改失败，旧密码错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/updateUserPasswordProtect")
    public Result updateUserProtectQuestion(@RequestBody PostUpdateProtectQuestionVO postUpdateProtectQuestionVO)
    {
        log.info("修改密保问题");
        try {
            boolean result = userService.updateUserProtectQuestion(postUpdateProtectQuestionVO.getUsername(), postUpdateProtectQuestionVO);
            if (result) {
                log.info("密保问题修改成功");
                return Result.success("密保问题修改成功",null);
            } else {
                log.info("密保问题修改失败，旧密码错误");
                return Result.error("密保问题修改失败，旧密码错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }


}
