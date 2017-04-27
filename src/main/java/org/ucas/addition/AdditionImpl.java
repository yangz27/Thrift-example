package org.ucas.addition;

/**
 * Author: Lucio.yang
 * Date: 17-4-27
 */
public class AdditionImpl implements AdditionService.Iface{
    public AdditionImpl(){

    }

    @Override
    public int add(int a,int b){
        return a+b;
    }
}
