package m5;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.158.140");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Channel c = f.newConnection().createChannel();
        f.setVirtualHost("lee");  //自定义空间；

        // 创建交互机 Direct 交换机： topic_logs
        c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);

        // 发送消息，携带路由键关键词
        while (true){
            System.out.println("输入消息：");
            String s = new Scanner(System.in).nextLine();
            System.out.println("输入路由键：");
            String k = new Scanner(System.in).nextLine();
            /*
            * 第二个参数是路由键关键词
            * 在简单模式和工作模式中，使用的默认交换机自动使用队列名做绑定*/
            c.basicPublish("topic_logs",k,null,s.getBytes());
        }
    }
}
