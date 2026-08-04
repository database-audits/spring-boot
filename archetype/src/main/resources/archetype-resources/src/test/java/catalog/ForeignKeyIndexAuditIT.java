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
import io.github.databaseaudits.spring.boot.assertion.ForeignKeyIndexAuditAssertion;

/**
 * Asserts that every foreign key in the schema has a supporting index, with a place to exclude one that is
 * deliberately unindexed.
 */
#if($parentClass && $parentClass != '' && $parentClass != 'none')
public class ForeignKeyIndexAuditIT extends ${simpleParentClass} {
#else
public class ForeignKeyIndexAuditIT extends AbstractDatabaseAuditIT {
#end
    /** Exclude a deliberately unindexed FK, e.g. Set.of("fk_orders_customer_legacy"). */
    private static final Set<String> EXCLUDED_CONSTRAINTS = Set.of();

    @Autowired
    private ForeignKeyIndexAuditAssertion foreignKeyIndexAuditAssertion;

    @Value("#[[${]]#${schemaPropertyName}#[[}]]#")
    private String schema;

    @Test
#if($disabledTests == 'true')
    @Disabled("Generated as disabled; remove @Disabled to enable")
#end
    void testEveryForeignKeyHasSupportingIndex() {
        foreignKeyIndexAuditAssertion.assertClean(schema, EXCLUDED_CONSTRAINTS);
    }
}
