package com.bobocode;

public class ProxyClass {

    public void hello(){
        System.out.println("say hello");
    }

    @LogInvocation
    public void gloryToUkraine(){
        System.out.println("say glory to Ukraine");
    }
}
