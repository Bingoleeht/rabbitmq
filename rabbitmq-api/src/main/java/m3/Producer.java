package m3;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

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
        Channel c = f.newConnection().createChannel();

        //创建交换机 Fanout--> logs
        c.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);

        // 向logs 交换机发送消息
        while (true){
            System.out.println("输入消息：");
            String s = new Scanner(System.in).nextLine();
            // 对于 fanout 类型交换机，第二个参数无效;
            c.basicPublish("logs","",null,s.getBytes());
        }
    }
}
