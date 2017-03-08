package it.uniroma2.isssr.examples;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by mastro on 08/03/17.
 */

public interface MyEntityRepository extends JpaRepository<MyEntity, Long>, MyEntityRepositoryCustom {

    Set<MyEntity> findByName(String name);
}
