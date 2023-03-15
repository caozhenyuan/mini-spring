package com.minis.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 曹振远
 * @date 2023/03/15
 **/
public class ArgumentValues {

    private final List<ArgumentValue> argumentValueList = new ArrayList<>();

    public ArgumentValues() {
    }

    public void addArgumentValue(String type, String name, Object value){
        this.argumentValueList.add(new ArgumentValue(type,name,value));
    }
    public void addArgumentValue(ArgumentValue argumentValue) {
        this.argumentValueList.add(argumentValue);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
        return this.argumentValueList.get(index);
    }

    public int getArgumentCount() {
        return (this.argumentValueList.size());
    }

    public boolean isEmpty() {
        return (this.argumentValueList.isEmpty());
    }
}
