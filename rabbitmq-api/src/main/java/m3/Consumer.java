package m3;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接；
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.158.140");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Channel c = f.newConnection().createChannel();

        // 创建随机队列  创建交换机   绑定
        String queue = UUID.randomUUID().toString();
        c.queueDeclare(queue,false,true,true,null);
        c.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);
        c.queueBind(queue,"logs","");   //FANOUT 交换机，第三个参数无效;

        // 接收处理消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody());
            System.out.println("收到："+ s);
        };

        CancelCallback cancelCallback = consumerTag -> {};
        c.basicConsume(queue,true,deliverCallback,cancelCallback);
    }
}
