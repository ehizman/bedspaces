package data.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<K>{
    Optional<K> findById(String id) throws Exception;
    List<K> findByName(String name);
    K save(K k);
    void delete(String id);
    void delete(K k);
    List<K> findAll();
}
