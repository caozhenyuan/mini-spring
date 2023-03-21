package com.minis.beans.factory.config;

import com.minis.beans.factory.config.ConstructorArgumentValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 曹振远
 * @date 2023/03/15
 **/
public class ConstructorArgumentValues {

    private final List<ConstructorArgumentValue> argumentValueList = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(String type, String name, Object value){
        this.argumentValueList.add(new ConstructorArgumentValue(type,name,value));
    }
    public void addArgumentValue(ConstructorArgumentValue argumentValue) {
        this.argumentValueList.add(argumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return this.argumentValueList.get(index);
    }

    public int getArgumentCount() {
        return (this.argumentValueList.size());
    }

    public boolean isEmpty() {
        return (this.argumentValueList.isEmpty());
    }
}
