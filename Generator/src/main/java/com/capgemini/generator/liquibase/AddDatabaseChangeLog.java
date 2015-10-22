package com.capgemini.generator.liquibase;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;


public class AddDatabaseChangeLog {
    public AddDatabaseChangeLog() {
        super();
    }
    
    public DatabaseChangeLog addDatabaseChangeLog(Class databaseChangeLogClass) {
        
        DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();
        addChangeSets(databaseChangeLog, databaseChangeLogClass);
        return databaseChangeLog;
    }
    
    private void addChangeSets(DatabaseChangeLog databaseChangeLog, Class databaseChangeLogClass) {
        
        long index = 0;
        AddChangeSet changeSet = new AddChangeSet();
        databaseChangeLog.addChangeSet(changeSet.addTableChangeSet(databaseChangeLog, databaseChangeLogClass, index++));
        AddForeignKey addForeignKey = new AddForeignKey();
        ChangeSet fkChangeSet = addForeignKey.addForeignKeyChangeSet(databaseChangeLog, databaseChangeLogClass, index++);
        if(fkChangeSet != null) {
            databaseChangeLog.addChangeSet(fkChangeSet);
        }
    }   
}
