package m4;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.158.140");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Channel c = f.newConnection().createChannel();
        f.setVirtualHost("Bingo");  //自定义空间；

        // 1 创建随机队列 2 创建交换机 3 绑定 设置绑定键
        String queue  = UUID.randomUUID().toString();
        c.queueDeclare(queue,false,true,true,null);
        c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
        System.out.println("输入绑定键，用空格隔开： ");// aa  bb  cc
        String s = new Scanner(System.in).nextLine();
        String[] split = s.split("\\s+");//     \s 空白字符    + 一到多个
        for (String k : split) {
            c.queueBind(queue,"direct_logs",k);
        }

        // 从队列接收，处理消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody());
            String key = message.getEnvelope().getRoutingKey();
            System.out.println(key+"--"+msg);
        };
        CancelCallback cancelCallback = consumerTag -> {};
        c.basicConsume(queue,true,deliverCallback,cancelCallback);
    }
}
