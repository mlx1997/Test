package com.mr.client;


import com.mr.bo.AddressBo;
import org.springframework.stereotype.Component;

@Component
public class AddressClient {

    public AddressBo addressClient(Long id){
        return AddressBo.addreddMap.get(id);
    }
}
