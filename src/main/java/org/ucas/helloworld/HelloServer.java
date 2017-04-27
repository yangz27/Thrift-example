package org.ucas.helloworld;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

/**
 * Author: Lucio.yang
 * Date: 17-4-27
 * 单线程 & 阻塞式IO
 */
public class HelloServer {
    public static final int SERVER_PORT = 8090;

    /**
     * @param args
     */
    public static void main(String[] args) {
        HelloServer server = new HelloServer();
        server.startServer();
    }

    public void startServer() {
        try {
            // 设置传输通道 - 普通IO流通道
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            // 设置处理器HelloWorldImpl
            HelloWorldService.Processor<HelloWorldService.Iface> processor =
                    new HelloWorldService.Processor<HelloWorldService.Iface>(
                            new HelloWorldImpl()
                    );
            // 使用高密度二进制协议
            TProtocolFactory proFactory = new TCompactProtocol.Factory();
            // 简单的单线程服务模型
            TServer server = new TSimpleServer(
                    new TSimpleServer.Args(serverTransport)
                            .protocolFactory(proFactory)
                            .processor(processor)
            );
            System.out.println("HelloWorld TSimpleServer started ....");
            server.serve();
        } catch (Exception e) {
            System.out.println("Server start error!");
            e.printStackTrace();
        }
    }
}
