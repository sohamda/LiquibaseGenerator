package com.capgemini.generator.liquibase;


import com.capgemini.generator.helper.ClassNamesHolder;
import com.capgemini.generator.helper.SystemProperties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;


public class AddColumn {
    
    public AddColumn() {
        super();
    }
    
    public ColumnConfig addColumn(Field field) {
        
        ColumnConfig columnConfig = new ColumnConfig();
        columnConfig.setName(field.getName().toUpperCase());
        columnConfig.setType(getColumnType(field));
        
        if(field.isAnnotationPresent(NotNull.class)) {
            ConstraintsConfig constraintsConfig = new ConstraintsConfig();
            constraintsConfig.setNullable(Boolean.FALSE);
            columnConfig.setConstraints(constraintsConfig);
        }
        return columnConfig;
    }
    
    public ColumnConfig addIDColumn() {
        
        ColumnConfig columnConfig = new ColumnConfig();
        columnConfig.setName("ID");
        columnConfig.setType("Number(14,0)");
        return columnConfig;
    }
    
    private String getColumnType(Field changeSetField) {
        
        String columnType = SystemProperties.getInstance().getProperty(changeSetField.getType().getSimpleName());
        if(columnType == null) {
            if(ClassNamesHolder.getInstance().getClassNames().contains(changeSetField.getType().getName())) {
                columnType = "Number(14,0)";
            } else {
                throw new RuntimeException("Could not match : " + changeSetField.getType().getSimpleName() + " to any Database type, please check the JavaTypeDBTypaMapping.properties");
            }
        } 
        if(columnType.equals("varchar2") || columnType.equals("Number")) {
            columnType = columnType + getSize(changeSetField);
        }
                
        return columnType;
    }

    private String getSize(Field changeSetField) {
        String size = "";
        if(changeSetField.isAnnotationPresent(Size.class)) {
            Annotation annotation = changeSetField.getAnnotation(Size.class);
            int max = ((Size)annotation).max();
            switch(changeSetField.getType().getSimpleName()) {
            case "String" :
                size = "(" + max + ")";
                break;
            case "BigDecimal" :
                size = "(" + max + ", 2)";
                break;
            default : 
                size = "(" + max + ", 0)";
                break;
            }
        }
        return size;
    }
}
