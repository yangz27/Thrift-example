package org.ucas.addition;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Author: Lucio.yang
 * Date: 17-4-27
 */
public class AdditionClient {
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8090;
    public static final int TIMEOUT = 30000;

    /**
     * @param args
     */
    public static void main(String[] args) {
        AdditionClient client = new AdditionClient();
        client.startClient(1,2);

    }

    /**
     * @param a
     * @param b
     */
    public void startClient(int a,int b) {
        TTransport transport = null;
        try {
            // 设置传输通道 - 普通IO流通道
            transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
            // 协议要和服务端一致
            TProtocol protocol = new TCompactProtocol(transport);
            // 创建服务器，启动
            AdditionService.Client client = new AdditionService.Client(
                    protocol);
            transport.open();
            int result = client.add(a,b);
            System.out.println("Thrift client result =: " + result);
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
