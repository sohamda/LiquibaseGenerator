package com.capgemini.generator.helper;

import java.util.ArrayList;
import java.util.List;


public class ClassNamesHolder {
    
    private static ClassNamesHolder classNamesHolder = null;
    private List<String> classNames = new ArrayList<>();
    
    protected ClassNamesHolder() {
        super();
    }
    
    public static ClassNamesHolder getInstance() {
        if(classNamesHolder == null) {
            classNamesHolder = new ClassNamesHolder();
        }
        return classNamesHolder;
    }
    
    public boolean addClassName(String className) {    
        return classNames.add(className);
    }

    public List<String> getClassNames() {
        return classNames;
    }
}
