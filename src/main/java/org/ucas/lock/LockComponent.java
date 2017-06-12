package org.ucas.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Lucio.yang
 * Date: 17-4-27
 * 锁组件
 */
public class LockComponent {
    /**
     * 可重入读写锁
     */
    public ReentrantReadWriteLock rwLock;
    /**
     * 读锁
     */
    public Lock rLock;
    /**
     * 写锁
     */
    public Lock wLock;
    /**
     * 拥有者的事务ID
     */
    public String uuid;
    /**
     * 锁占用状态
     */
    public Boolean flag;

    public LockComponent(){
        this.rwLock=new ReentrantReadWriteLock();
        this.rLock=rwLock.readLock();
        this.wLock=rwLock.writeLock();
        this.uuid="";
        this.flag=false;
    }
}
