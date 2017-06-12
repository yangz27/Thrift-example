package org.ucas.lock;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.UUID;

/**
 * Author: Yang Zhao
 * Date: 17-4-27
 * 锁客户端
 */
public class LockClient {
    /**
     * 服务器IP
     */
    public static final String SERVER_IP = "localhost";
    /**
     * 服务器监听端口
     */
    public static final int SERVER_PORT = 8099;
    /**
     * 超时时间
     */
    public static final int TIMEOUT = 30000;

    /**
     * @param args
     */
    public static void main(String[] args) {
        LockClient client = new LockClient();
        client.startClient();

    }

    /**
     * 开启一个线程池，模拟分布式多进程访问
     */
    public void startClient() {
        TTransport transport = null;
        try {
            // 设置传输通道 - 普通IO流通道
            transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
            // 协议要和服务端一致
            TProtocol protocol = new TCompactProtocol(transport);
            // 创建服务器，启动
            LockService.Client client = new LockService.Client(
                    protocol);
            transport.open();
            // 客户端每个消息必须附带事务ID：
            // 未收到答复的请求，重传时，事务ID相同;
            // 只有当获取锁成功后，事务ID不变，作为释放锁时的事务ID
            // 获取失败或释放失败，下次事务ID重新生成
            String uuid1= UUID.randomUUID().toString();
            String uuid2= UUID.randomUUID().toString();
            for( int i=0;i<3;i++ ){
                System.out.println("#Thrift client "+uuid1+" acquire lock =: " + client.acquire(uuid1));
                System.out.println("#Thrift client "+uuid2+" acquire lock =: " + client.acquire(uuid2));
            }
            for( int i=0;i<3;i++ ){
                System.out.println("#Thrift client "+uuid1+" release lock =: " + client.release(uuid1));
                System.out.println("#Thrift client "+uuid2+" release lock =: " + client.release(uuid2));
            }

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }
}
