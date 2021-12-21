package m1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接服务器
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.158.140");
        f.setPort(5672); //5672 是客户端收发消息的端口，15672是管理控制台的访问端口；
        f.setUsername("admin");
        f.setPassword("admin");
        Connection con = f.newConnection();
        Channel c = con.createChannel();    // 通信的通道；

        //创建队列
        c.queueDeclare("helloworld",false,false,false,null);
        // 创建回调对象
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            byte[] a = message.getBody();
            String s = new String(a);
            System.out.println("收到 "+ s);
        };
        CancelCallback cancelCallback = consumerTag -> {};
        //从helloworld接收消息
        /**
         * 第二个参数：true  自动确认
         */
        c.basicConsume("helloworld",true,deliverCallback,cancelCallback);
    }
}
