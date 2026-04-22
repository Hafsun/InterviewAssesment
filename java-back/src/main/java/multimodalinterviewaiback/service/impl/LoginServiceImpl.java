package multimodalinterviewaiback.service.impl;

import multimodalinterviewaiback.mapper.LoginMapper;
import multimodalinterviewaiback.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;


    @Override
    public String getPassProtect(String username) {
        return loginMapper.getPassProtectByUsername(username);
    }

    @Override
    public boolean judgePassProtectAnswer(String username, String passProtectAnswer) {
        String correctAnswer = loginMapper.getPassProtectAnswerByUsername(username);
        return passProtectAnswer.equals(correctAnswer);
    }

    @Override
    public boolean updatePasswordByProtectAnswer(String username, String newPassword) {
        int rows = loginMapper.updatePasswordByUsername(username, newPassword);
        return rows > 0;
    }
}
