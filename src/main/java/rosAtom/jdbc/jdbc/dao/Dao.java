package rosAtom.jdbc.jdbc.dao;

public interface Dao <T>{

    void create(T t);

    void update (Long id, T t);

    void delete(Long id);

    T findById(Long id);
}
