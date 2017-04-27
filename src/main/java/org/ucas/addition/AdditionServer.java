package org.ucas.addition;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.ucas.helloworld.HelloServer;

/**
 * Author: Lucio.yang
 * Date: 17-4-27
 * 多线程 & 阻塞式IO
 */
public class AdditionServer {
    public static final int SERVER_PORT = 8090;

    /**
     * @param args
     */
    public static void main(String[] args) {
        AdditionServer server = new AdditionServer();
        server.startServer();
    }

    public void startServer() {
        try {
            // 设置传输通道 - 普通IO流通道
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            // 设置处理器HelloWorldImpl
            AdditionService.Processor<AdditionService.Iface> processor =
                    new AdditionService.Processor<AdditionService.Iface>(
                            new AdditionImpl()
                    );
            // 使用高密度二进制协议
            TProtocolFactory proFactory = new TCompactProtocol.Factory();
            // 多线程服务模型
            TServer server = new TThreadPoolServer(
                    new TThreadPoolServer.Args(serverTransport)
                            .protocolFactory(proFactory)
                            .processor(processor)
            );
            System.out.println("Addition TThreadPoolServer started ....");
            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error!");
            e.printStackTrace();
        }
    }
}
