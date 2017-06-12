package org.ucas.lock;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Lucio.yang
 * Date: 17-4-27
 * 锁服务器
 */
public class LockServer {
    /**
     * 锁服务器监听端口
     */
    private static final int SERVER_PORT = 8099;

    /**
     * @param args
     */
    public static void main(String[] args) {
        LockServer server = new LockServer();
        server.startServer();
    }

    /**
     * 启动服务器进程
     */
    public void startServer() {
        try {
            // 共享锁组件，用以实现一把锁
            LockComponent lc=new LockComponent();
            // 哈希表，记录历史消息，实现at-most-once
            Map<String,Result> record=new HashMap<String, Result>();

            // 设置传输通道 - 普通IO流通道
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            // 设置处理器LockImpl
            LockService.Processor<LockService.Iface> processor =
                    new LockService.Processor<LockService.Iface>(
                            new LockImpl(lc,record)
                    );
            // 使用高密度二进制协议
            TProtocolFactory proFactory = new TCompactProtocol.Factory();
            // 多线程服务模型
            TServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport)
                            .protocolFactory(proFactory)
                            .processor(processor)
            );
            System.out.println("#Lock Server started ....");
            server.serve();

        } catch (Exception e) {
            System.out.println("#Server start error!");
            e.printStackTrace();
        }
    }
}
