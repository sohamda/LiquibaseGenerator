package com.capgemini.generator.liquibase;


import com.capgemini.generator.helper.ClassNamesHolder;
import com.capgemini.generator.helper.SystemProperties;

import java.lang.reflect.Field;

import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.change.core.CreateIndexChange;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;


public class AddForeignKey {
    
    public AddForeignKey() {
        super();
    }
    
    public ChangeSet addForeignKeyChangeSet(DatabaseChangeLog databaseChangeLog, Class databaseChangeLogClass, Long index) {
        
        ChangeSet changeSet = new ChangeSet(index.toString(), "liquibaseGenerator", false, false, "", null, null, true, null, databaseChangeLog);
        int fkIndex = 1;
        for(Field field : databaseChangeLogClass.getDeclaredFields()) {
            String fieldType = field.getType().getSimpleName();
            if(SystemProperties.getInstance().getProperty(fieldType) == null && ClassNamesHolder.getInstance().getClassNames().contains(field.getType().getCanonicalName())) {
                changeSet.addChange(addForeignKeyChange(databaseChangeLogClass, fieldType, field.getName(), fkIndex));
                changeSet.addChange(addIndexChange(databaseChangeLogClass, field.getName(), fkIndex));
                ++fkIndex;
            }
        }
        if(changeSet.getChanges().isEmpty()) {
            return null;
        }
        return changeSet;
    }

    private AddForeignKeyConstraintChange addForeignKeyChange(Class databaseChangeLogClass, String fieldType, String fieldName, int fkIndex) {
        
        AddForeignKeyConstraintChange fkConstraintChange = new AddForeignKeyConstraintChange();
        fkConstraintChange.setBaseColumnNames(fieldName.toUpperCase());
        fkConstraintChange.setBaseTableName(databaseChangeLogClass.getSimpleName().toUpperCase());
        fkConstraintChange.setConstraintName(databaseChangeLogClass.getSimpleName().toUpperCase() + "_FK" + fkIndex);
        
        fkConstraintChange.setReferencedTableName(fieldType.toUpperCase());
        fkConstraintChange.setReferencedColumnNames("ID");
        fkConstraintChange.setDeferrable(Boolean.TRUE);
        
        return fkConstraintChange;
    }

    private CreateIndexChange addIndexChange(Class databaseChangeLogClass, String fieldName, int fkIndex) {
        
        AddColumnConfig column = new AddColumnConfig();
        column.setName(fieldName.toUpperCase());
        
        CreateIndexChange createIndexChange = new CreateIndexChange();
        createIndexChange.setTableName(databaseChangeLogClass.getSimpleName().toUpperCase());
        createIndexChange.setIndexName(databaseChangeLogClass.getSimpleName().toUpperCase() + "_FK" + fkIndex);
        createIndexChange.addColumn(column);
        
        return createIndexChange;
    }
}
