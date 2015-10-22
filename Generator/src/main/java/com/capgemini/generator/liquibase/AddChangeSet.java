package com.capgemini.generator.liquibase;

import liquibase.change.AbstractChange;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.CreateSequenceChange;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;


public class AddChangeSet {
    public AddChangeSet() {
        super();
    }
    
    public ChangeSet addTableChangeSet(DatabaseChangeLog databaseChangeLog, Class databaseChangeLogClass, Long index) {
        
        ChangeSet changeSet = new ChangeSet(index.toString(), "liquibaseGenerator", false, false, "", null, null, true, null, databaseChangeLog);
        changeSet.addChange(addTableChange(databaseChangeLogClass));
        changeSet.addChange(addPrimaryKeyChange(databaseChangeLogClass));
        changeSet.addChange(addSequence(databaseChangeLogClass));
        return changeSet;
    }
    
    private AbstractChange addTableChange(Class databaseChangeLogClass) {
        
        AddTable addTable = new AddTable();
        return addTable.addTable(databaseChangeLogClass);
    }
    
    private AbstractChange addPrimaryKeyChange(Class databaseChangeLogClass) {
        
        AddPrimaryKeyChange addPK = new AddPrimaryKeyChange();
        addPK.setTableName(databaseChangeLogClass.getSimpleName().toUpperCase());
        addPK.setColumnNames("ID");
        addPK.setConstraintName(databaseChangeLogClass.getSimpleName().toUpperCase()+"_PK");        
        return addPK;
    }
    
    private AbstractChange addSequence(Class databaseChangeLogClass) {        
        CreateSequenceChange addSeq = new CreateSequenceChange();        
        addSeq.setSequenceName(databaseChangeLogClass.getSimpleName().toUpperCase()+"_SEQ");        
        return addSeq;
    }
    
}
