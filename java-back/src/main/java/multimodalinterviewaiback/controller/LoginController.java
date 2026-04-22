package multimodalinterviewaiback.controller;

import multimodalinterviewaiback.common.BaseContext;
import multimodalinterviewaiback.pojo.entity.Result;
import multimodalinterviewaiback.pojo.entity.User;
import multimodalinterviewaiback.pojo.vo.login.*;
import multimodalinterviewaiback.service.LoginService;
import multimodalinterviewaiback.service.UserService;
import multimodalinterviewaiback.tools.JwtTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody // 返回JSON
@CrossOrigin("*") // 允许跨域
@RequestMapping("/api")
@Slf4j
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Autowired
    JwtTool jwtTool;



    @PostMapping("/login")
    public Result login(@RequestBody PostLoginVO postLoginVO){
        log.info("用户 {} 请求登录",postLoginVO.getUsername());
        int userCount = userService.findUserCountByUsername(postLoginVO.getUsername());
        String password = userService.getUserPassword(postLoginVO.getUsername());
        if(userCount != 0 && password != null) {
            if(password.equals(postLoginVO.getPassword())) {
                Long userId = userService.getUserId(postLoginVO.getUsername(), postLoginVO.getPassword());
                // 创建用户信息映射MAP 用于创建Token
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("username", postLoginVO.getUsername());
                userInfo.put("password", postLoginVO.getPassword());
                userInfo.put("userId", userId);
                // 发放Token于前端
                String jwt = jwtTool.makeJwt(userInfo);
                ReturnLoginVO returnLoginVO = new ReturnLoginVO();
                returnLoginVO.setJwt(jwt);

                ReturnGetUserInfo returnGetUserInfo = userService.getUserInfo(postLoginVO.getUsername());

                returnLoginVO.setName(returnGetUserInfo.getName());
                returnLoginVO.setEmail(returnGetUserInfo.getEmail());
                returnLoginVO.setPhone(returnGetUserInfo.getPhone());
                returnLoginVO.setMajor(returnGetUserInfo.getMajor());
                returnLoginVO.setUsername(returnGetUserInfo.getUsername());
                returnLoginVO.setComprehensiveScore(returnGetUserInfo.getComprehensiveScore());
                returnLoginVO.setInterviewCount(returnGetUserInfo.getInterviewCount());
                returnLoginVO.setSex(returnLoginVO.getSex());
                returnLoginVO.setPictureUrl(returnGetUserInfo.getPictureUrl());
                log.info(postLoginVO.getUsername()+"：登录成功");
                return Result.success("登录成功", returnLoginVO);
            }
            else{
                log.info(postLoginVO.getUsername()+"登录失败：密码错误");
                return Result.error("密码错误");
            }

        }
        else {
            log.info(postLoginVO.getUsername()+"登录失败：用户不存在");
            return Result.error("用户不存在");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody PostRegisterVO postRegisterVO)
    {
        try{
            User user = new User();
            user.setUsername(postRegisterVO.getUsername());
            user.setPassword(postRegisterVO.getPassword());
            user.setPassProtect(postRegisterVO.getPassProtect());
            user.setPassProtectAnswer(postRegisterVO.getPassProtectAnswer());

            if(userService.findUserCountByUsername(user.getUsername()) == 0)
            {
                userService.addUser(user);
                log.info("{}:注册成功", user.getUsername());
                return Result.success("注册成功", null);
            }else{
                log.info("{}:注册失败，用户名已存在", user.getUsername());
                return Result.error("用户名已存在");
            }
        }catch(Exception e){
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/getPassProtect")
    public Result getPassProtect(@RequestBody PostGetPassProtectVO postGetPassProtectVO) {
        log.info("查询用户：{} 密保问题", postGetPassProtectVO.getUsername());
        try{
            String passProtect = loginService.getPassProtect(postGetPassProtectVO.getUsername());
            if (passProtect != null) {
                return Result.success(passProtect);
            } else {
                return Result.error("未找到该用户的密保问题");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }

    }

    @PostMapping("/forgotPassword")
    public Result judgePassProtectAnswer(@RequestBody PostAnswerAndPassVO postAnswerAndPassVO) {
        log.info("判断密保问题是否正确");
        try{
            String username = postAnswerAndPassVO.getUsername();
            String passProtectAnswer = postAnswerAndPassVO.getPassProtectAnswer();
            String newPassword = postAnswerAndPassVO.getNewPassword();
            boolean isCorrect = loginService.judgePassProtectAnswer(username, passProtectAnswer);
            if (isCorrect) {
                boolean isUpdated = loginService.updatePasswordByProtectAnswer(username, newPassword);
                if (isUpdated) {
                    return Result.success("密码更新成功",null);
                } else {
                    return Result.error("密码更新失败");
                }
            } else {
                log.info("密保错误");
                return Result.error("密保答案错误");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error(e.getMessage());
        }

    }
}
