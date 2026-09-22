package org.example.classes;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class personInvocationHandler implements InvocationHandler {
    private Person person;
    public personInvocationHandler(Person person) {
        this.person = person;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        System.out.println("hi");
        return null;
    }
}
