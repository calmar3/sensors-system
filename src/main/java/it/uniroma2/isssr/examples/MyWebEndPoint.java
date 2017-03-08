package it.uniroma2.isssr.examples;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by mastro on 08/03/17.
 */
@RestController
public class MyWebEndPoint {

    @Inject
    private MyEntityRepository myEntityRepository;

    @RequestMapping(path = "myentity", method = RequestMethod.GET)
    public MyEntity prelevaEntity(@RequestParam Long id) {
        MyEntity found = myEntityRepository.findOne(id);
        return found;
    }


    @RequestMapping(path = "myentity", method = RequestMethod.POST)
    public MyEntity salvaEntity(@RequestBody MyEntity myEntity) {
        return myEntityRepository.save(myEntity);
    }

    @RequestMapping(path = "myentity/findBy/{name}", method = RequestMethod.GET)
    public Set<MyEntity> findEntity(@PathVariable String name) {
        Set<MyEntity> found = myEntityRepository.findByName(name);
        return found;
    }
}
