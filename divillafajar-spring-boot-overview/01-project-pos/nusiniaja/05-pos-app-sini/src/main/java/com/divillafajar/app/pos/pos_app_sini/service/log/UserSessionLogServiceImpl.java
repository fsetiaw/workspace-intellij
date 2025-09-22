package com.divillafajar.app.pos.pos_app_sini.service.log;

import com.divillafajar.app.pos.pos_app_sini.io.entity.auth.NamePassEntity;
import com.divillafajar.app.pos.pos_app_sini.io.entity.session.UserSessionLogDTO;
import com.divillafajar.app.pos.pos_app_sini.repo.users.UsersRepo;
import org.springframework.stereotype.Service;

@Service
public class UserSessionLogServiceImpl implements UserSessionLogService{

    private final UsersRepo usersRepo;

    public UserSessionLogServiceImpl(UsersRepo usersRepo) {
        this.usersRepo=usersRepo;
    }

    @Override
    public UserSessionLogDTO prepUserSessionLog(String username) {
        System.out.println("PrepUserSessionLog IS CALLED");
        UserSessionLogDTO retunVal = new UserSessionLogDTO();
        NamePassEntity stored = usersRepo.findUsersByUsername(username);
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
