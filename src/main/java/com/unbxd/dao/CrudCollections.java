package dao;

import com.mongodb.client.MongoCollection;
import com.unbxd.model.Student;
import org.bson.Document;

public interface CrudCollections {
    void insetInTo(MongoCollection collection);
    Student readCollection(MongoCollection collection, Integer id);
    void updateCollection(MongoCollection collection, Integer id);
    long deleteCollection(MongoCollection collection, Integer id);
    void insetNew(MongoCollection<Document> collection, Student student);
}
