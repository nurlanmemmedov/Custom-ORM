package interfaces;
import storages.Entity;
import java.util.List;

public interface IEntityManager {
    int create(Entity entity);
    void updateById(Entity entity, int value);
    boolean deleteById(Entity entity, int value);
    boolean deleteAll(Entity entity);
    List<Entity> getAll(Entity entity);
    Entity find(Entity entity, int id);

}
