package it.uniroma2.isssr.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

public class MyEntityRepositoryImpl implements MyEntityRepositoryCustom {

    @Autowired
    MyEntityRepository accountRepository;  /* Optional - if you need it */

    public void customMethod() {  }
}