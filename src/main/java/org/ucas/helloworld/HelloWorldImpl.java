package org.ucas.helloworld;

/**
 * Author: Lucio.yang
 * Date: 17-4-27
 */
public class HelloWorldImpl implements HelloWorldService.Iface {
    public HelloWorldImpl() {
    }

    @Override
    public String sayHello(String username) {
        return "Hi," + username + " ,Welcome to the thrift's world !";
    }
}
