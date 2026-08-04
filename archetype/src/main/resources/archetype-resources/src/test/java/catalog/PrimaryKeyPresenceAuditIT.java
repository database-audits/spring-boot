package ${package}.catalog;

import java.util.Set;

#if($disabledTests == 'true')
import org.junit.jupiter.api.Disabled;
#end
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

#if($parentClass && $parentClass != '' && $parentClass != 'none')
#set($simpleParentClass = $parentClass.replaceAll('.*\.', ''))
import ${parentClass};
#else
import ${package}.AbstractDatabaseAuditIT;
#end
import io.github.databaseaudits.audit.catalog.PrimaryKeyPresenceAudit;
import io.github.databaseaudits.spring.boot.assertion.PrimaryKeyPresenceAuditAssertion;

/**
 * Asserts that every base table in the schema has a primary key, excluding Liquibase's own bookkeeping tables by
 * default.
 */
#if($parentClass && $parentClass != '' && $parentClass != 'none')
public class PrimaryKeyPresenceAuditIT extends ${simpleParentClass} {
#else
public class PrimaryKeyPresenceAuditIT extends AbstractDatabaseAuditIT {
#end
    /** Liquibase's own bookkeeping tables have no primary key by design; add your own genuinely-PK-less tables
     *  the same way. */
    private static final Set<String> EXCLUDED_TABLES =
            PrimaryKeyPresenceAudit.LIQUIBASE_BOOKKEEPING_TABLES;

    @Autowired
    private PrimaryKeyPresenceAuditAssertion primaryKeyPresenceAuditAssertion;

    @Value("#[[${]]#${schemaPropertyName}#[[}]]#")
    private String schema;

    @Test
#if($disabledTests == 'true')
    @Disabled("Generated as disabled; remove @Disabled to enable")
#end
    void testEveryBaseTableHasPrimaryKey() {
        primaryKeyPresenceAuditAssertion.assertClean(schema, EXCLUDED_TABLES);
    }
}
