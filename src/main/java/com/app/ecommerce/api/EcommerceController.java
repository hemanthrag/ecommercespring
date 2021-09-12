package com.app.ecommerce.api;

import com.app.ecommerce.model.LoginModel;
import com.app.ecommerce.model.Users;
import com.app.ecommerce.service.EcommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

@RequestMapping("api")
@RestController
public class EcommerceController {
    private EcommerceService ecommerceService;

    @Autowired
    public EcommerceController(EcommerceService ecommerceService){
        this.ecommerceService = ecommerceService;
    }

    @PostMapping(path = "/auth/createuser")
    public HashMap<String, String> addUser(@Valid @NotNull @RequestBody Users users){
        return ecommerceService.addUser(users);
    }

    @PostMapping(path = "/login")
    public HashMap<String, String> loginUser(@Valid @NotNull @RequestBody LoginModel loginModel){
        return ecommerceService.loginUser(loginModel.getEmailPhone(),loginModel.getPassword());
    }

    @GetMapping(path = "/getAllUser")
    public List<Users> getUsersData(){
        return ecommerceService.getUsersData();
    }
}
