package com.capgemini.generator.liquibase;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;

import liquibase.change.ColumnConfig;
import liquibase.change.core.CreateTableChange;


public class AddTable {
    
    public AddTable() {
        super();
    }
    
    public CreateTableChange addTable(Class databaseChangeLogClass) {
        
        CreateTableChange createTable = new CreateTableChange();
        createTable.setTableName(databaseChangeLogClass.getSimpleName().toUpperCase());        
        createTable.setColumns(addColumns(databaseChangeLogClass.getDeclaredFields()));
        return createTable;
    }
    
    private List<ColumnConfig> addColumns(Field[] declaredFields) {
        
        List<ColumnConfig> columnConfigs = new ArrayList<>();
        AddColumn addColumn = new AddColumn();
        columnConfigs.add(addColumn.addIDColumn());
        for(Field field : declaredFields) {
            columnConfigs.add(addColumn.addColumn(field));
        }        
        return columnConfigs;
    }
}
