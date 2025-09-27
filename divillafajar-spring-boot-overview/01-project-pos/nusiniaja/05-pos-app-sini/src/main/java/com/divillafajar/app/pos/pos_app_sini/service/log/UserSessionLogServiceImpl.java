package com.divillafajar.app.pos.pos_app_sini.service.log;

import com.divillafajar.app.pos.pos_app_sini.exception.user.UserNotFoundException;
import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.client.ClientEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLogDTO;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.UserEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.user.dto.UserDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.client.ClientRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.user.UserRepo;
import com.divillafajar.app.pos.pos_app_sini.repo.users.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionLogServiceImpl implements UserSessionLogService{

    private final UsersRepo authRepo;
    private final UserRepo userRepo;
    private final ClientRepo clientRepo;
    private final MessageSource messageSource;

    //public UserSessionLogServiceImpl(authRepo authRepo) {
    //    this.authRepo=authRepo;
    //}

    @Override
    public UserSessionLogDTO prepUserSessionLog(String username) {
        System.out.println("PrepUserSessionLog IS CALLED");
        UserSessionLogDTO retunVal = new UserSessionLogDTO();
        NamePassEntity stored = authRepo.findUsersByUsername(username);
        Optional<ClientEntity> clientUser = clientRepo.findById(stored.getClient().getId());
        if(clientUser.isEmpty())
            throw new UserNotFoundException(messageSource.getMessage("error.client.notFound", null, LocaleContextHolder.getLocale()));
        BeanUtils.copyProperties(clientUser.get(),retunVal);
        retunVal.setClientPid(clientUser.get().getPubId());

        Optional<UserEntity> loginUserEntity = userRepo.findById(stored.getUser().getId());
        if(loginUserEntity.isEmpty())
            throw new UserNotFoundException(messageSource.getMessage("error.user.notFound", null, LocaleContextHolder.getLocale()));
        retunVal.setUserPid(loginUserEntity.get().getPubId());
        retunVal.setFullName(loginUserEntity.get().getFirstName().trim()+" "+loginUserEntity.get().getLastName().trim());
        System.out.println("retunVal=="+retunVal.getSessionId());
        System.out.println("retunVal=="+retunVal.getUserPid());
        System.out.println("retunVal=="+retunVal.getRole());
        System.out.println("retunVal=="+retunVal.getUsername());
        System.out.println("retunVal=="+retunVal.getClientPid());
        System.out.println("retunVal=="+retunVal.getClientListAliasName());
        System.out.println("retunVal=="+retunVal.getClientName());
        System.out.println("retunVal=="+retunVal.getClientType());
        System.out.println("retunVal=="+retunVal.getFullName());
        System.out.println("retunVal=="+retunVal.getLoginTime());
        /*
        if(stored!=null) {
            System.out.println("stored=="+stored.getEmployment());
            System.out.println("stored=="+stored.getEmployment().getClientAddress());
            System.out.println("stored=="+stored.getEmployment().getClientAddress().getId());
        }
        else {
            System.out.println("STORED IS NULL");
        }

         */

        if(stored.getGuest()!=null) {
            /*
            retunVal.setUserPid(stored.getGuest().getCustomer().getUser().getPubId());
            retunVal.setFullName((stored.getGuest().getCustomer().getUser().getFirstName()+" "+stored.getGuest().getCustomer().getUser().getLastName()).trim());

             */
        }
        else if(stored.getEmployment()!=null) {
            /*
            retunVal.setEmployeeClientId(stored.getEmployment().getClientAddress().getClient().getId());
            retunVal.setUserPid(stored.getEmployment().getEmployee().getUser().getPubId());
            System.out.println("fullname = "+(stored.getEmployment().getEmployee().getUser().getFirstName()+" "+stored.getEmployment().getEmployee().getUser().getLastName()).trim());
            retunVal.setFullName((stored.getEmployment().getEmployee().getUser().getFirstName()+" "+stored.getEmployment().getEmployee().getUser().getLastName()).trim());

             */
        }
        return retunVal;
    }
}
