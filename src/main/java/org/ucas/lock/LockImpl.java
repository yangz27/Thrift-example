package org.ucas.lock;

import com.sun.prism.impl.Disposer;
import sun.security.pkcs11.wrapper.CK_SSL3_RANDOM_DATA;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Lucio.yang
 * Date: 17-4-27
 * 服务实现
 */
public class LockImpl implements LockService.Iface{
    /**
     * 共享锁组件，实现一把锁
     */
    private LockComponent lc;
    /**
     * 哈希表，记录消息的事务ID和对应结果，来过滤重复请求
     */
    private Map<String,Result> record;

    public LockImpl(LockComponent _lc,Map<String,Result> _record){
        lc=_lc;
        record=_record;
    }

    /**
     * 过滤重复请求
     * @param uuid 事务ID
     * @param msgType 消息类型：请求锁或释放锁
     * @return
     */
    private boolean at_most_once(String uuid,int msgType){
        Result r=record.get(uuid);
        if( r==null )// 没有重复消息
            return false;
        else if( r.getMsgType()!=msgType )// 上一次消息是请求锁，此次是释放锁
            return false;
        else
            return true;
    }

    /**
     * 获取锁
     * @param uuid 事务ID
     * @return
     */
    @Override
    public boolean acquire(String uuid){
        if( at_most_once(uuid,1) ) {// 过滤重复请求
            System.out.println("重复acquire "+uuid+" "+record.get(uuid).getR());
            return record.get(uuid).getR();
        }
        // 1:锁被占用，2：锁可用，3：成功
        int flag;
        // 获取读锁，读取锁组件状态
        lc.rLock.lock();
        try {
            if( lc.flag ){
                flag=1;
            }else
                flag=2;
        }finally {
            lc.rLock.unlock();
        }
        switch(flag){
            case 1:record.put(uuid,new Result(false,1));return false;
        }
        // 获取写锁，试图拿到锁
        lc.wLock.lock();
        try {
            if( !lc.flag ){// 未被抢占
                lc.flag=true;
                lc.uuid=uuid;
                flag=3;
            }
        }finally {
            lc.wLock.unlock();
        }
        if( flag==3 ){
            record.put(uuid,new Result(true,1));
            return true;
        }
        record.put(uuid,new Result(false,1));
        return false;
    }

    /**
     * 释放锁
     * @param uuid 事务ID
     * @return
     */
    @Override
    public boolean release(String uuid){
        if( at_most_once(uuid,2) ) {// 过滤重复请求
            System.out.println("重复release "+uuid+" "+record.get(uuid).getR());
            return record.get(uuid).getR();
        }
        // 1:锁未被占用，2：锁被其他人占用，3：锁被我占用
        int flag;
        // 获取读锁，读取锁组件状态
        lc.rLock.lock();
        try {
            if( !lc.flag ){
                flag=1;
            }else{
                flag=2;
                if( lc.uuid.equals(uuid) )
                    flag=3;
            }
        }finally {
            lc.rLock.unlock();
        }
        switch (flag){
            case 1:record.put(uuid,new Result(false,2));return false;
            case 2:record.put(uuid,new Result(false,2));return false;
        }
        // 获取写锁，释放锁
        lc.wLock.lock();
        try{
            // 一定是锁拥有者第一个进入
            lc.flag=false;
        }finally {
            lc.wLock.unlock();
        }
        record.put(uuid,new Result(true,2));
        return true;
    }
}
