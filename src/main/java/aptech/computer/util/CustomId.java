package aptech.computer.util;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

public class CustomId implements IdentifierGenerator {
    private String prefix;
    private final String  mid = "-";

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object obj)
            throws HibernateException {
        Entity entity = (Entity) obj.getClass().getDeclaredAnnotation(Entity.class);
        Table table = (Table) obj.getClass().getDeclaredAnnotation(Table.class);
        String tableName=obj.getClass().getSimpleName();
        if (!entity.name().equals("")){
            tableName = entity.name();
        }
        if (table!=null&&!table.name().equals("")){
            tableName = table.name();
        }
        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),tableName
        );

        Stream<?> ids = session.createQuery(query).stream();
        long max = ids.map(o -> String.valueOf(o).replace(prefix + mid, ""))
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L);

        return prefix + mid + (max + 1);
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
    }
}
