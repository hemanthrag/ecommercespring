package com.app.ecommerce.service;

import com.app.ecommerce.dao.EcommerceDao;
import com.app.ecommerce.model.DatabaseSequence;
import com.app.ecommerce.model.Users;
import org.bson.json.JsonObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class EcommerceService {
    EcommerceDao ecommerceDao;
    boolean emailPresent;
    Users users;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    public EcommerceService(@Qualifier("ecommerceDao") EcommerceDao ecommerceDao){
        this.ecommerceDao = ecommerceDao;
    }

    public HashMap<String, String> addUser(Users users){
        List<Users> allUsers = getUsersData();
        for(int i=0;i<allUsers.size();i++){
            if(allUsers.get(i).getEmail().equals(users.getEmail())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email Already Exists Try Different Email");
            }else if(allUsers.get(i).getPhoneNumber().equals(users.getPhoneNumber())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number Already Exists Try Different Phone Number");
            }
        }
        users.setId(getSequenceNumber(Users.SEQUENCE_NAME));
        users.setPassword(hashPassword(users.getPassword()));

        Users user = ecommerceDao.addUser(users);
        HashMap<String,String> hashmapUser = new HashMap<>();
        hashmapUser.put("id",String.valueOf(user.getId()));
        hashmapUser.put("name",user.getName());
        hashmapUser.put("email",user.getEmail());
        hashmapUser.put("phoneNumber",user.getPhoneNumber());
        return hashmapUser;
    }

    public HashMap<String,String> loginUser(String emailPhone,String password){
        List<Users> allUsers = getUsersData();
        for(int i=0;i<allUsers.size();i++){
            if(allUsers.get(i).getEmail().equals(emailPhone) || allUsers.get(i).getPhoneNumber().equals(emailPhone)){
                emailPresent = true;
                if(checkPasswordMatches(password,allUsers.get(i).getPassword())){
                    users = allUsers.get(i);
                }else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Entered Password is incorrect");
                }
                break;
            } else {
                emailPresent = false;
            }
        }
        if(!emailPresent){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email or Phone No Not Found");
        }else {
            HashMap<String,String> hashmapUser = new HashMap<>();
            hashmapUser.put("id",String.valueOf(users.getId()));
            hashmapUser.put("name",users.getName());
            hashmapUser.put("email",users.getEmail());
            hashmapUser.put("phoneNumber",users.getPhoneNumber());
            return hashmapUser;
        }
    }

    public List<Users> getUsersData(){
        return ecommerceDao.getUsersData();
    }

    private String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private boolean checkPasswordMatches(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword))
            return true;
        else
            return false;
    }


    public int getSequenceNumber(String sequenceName){
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq",1);
        DatabaseSequence counter = mongoOperations.findAndModify(query,update,options().returnNew(true).upsert(true), DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
