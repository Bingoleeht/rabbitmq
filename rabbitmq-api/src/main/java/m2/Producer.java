package m2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接；
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.158.140");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Connection con = f.newConnection();
        Channel c = con.createChannel();
        // 创建helloworld队列
        // c.queueDeclare("helloworld",false,false,false,null);
        c.queueDeclare("task_queue",true,false,false,null);
        // 发送消息
        while (true){
            System.out.println("输入消息： ");
            String s = new Scanner(System.in).nextLine();
            c.basicPublish("","task_queue", MessageProperties.PERSISTENT_BASIC,s.getBytes());
        }
    }
}
