package m1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接服务器
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.158.140");
        f.setPort(5672); //5672 是客户端收发消息的端口，15672是管理控制台的访问端口；
        f.setUsername("admin");
        f.setPassword("admin");
        Connection con = f.newConnection();
        Channel c = con.createChannel();    // 通信的通道；

        // 在服务器上，创建队列 helloworld，如果已经存在不会重复创建
        /**
         * 队列的是那个布尔属性：
         * -是否是持久队列
         * -是否是排他队列（独占队列）
         * -是否自动删除
         * null  队列的其他属性
         */
        c.queueDeclare("helloworld",false,false,false,null);   //队列的声明；

        // 向 helloworld 队列发送消息
        /*
        * 第一个参数""： 默认的交换机
        * 第三个参数 null: 消息的其他参数属性*/
        c.basicPublish("","helloworld",null,"Hello world!".getBytes());
        System.out.println("消息发送完成");
        c.close();
    }
}
