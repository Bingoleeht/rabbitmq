package m2;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
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
        // 创建helloworld队列
        c.queueDeclare("task_queue",true,false,false,null);

        // 创建回调对象
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String s = new String(message.getBody());
            System.out.println("收到： "+ s);
            //遍历字符串找 "." 字符
            for (int i = 0; i < s.length(); i++) {
                if(s.charAt(i)=='.'){
                    try {
                        Thread.sleep(1000);  // 遇到 .  暂停一秒，模拟耗时消息；
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //手动发送回执
            //c.basicAck(回执，是否确认之前收到过的所有消息);
            c.basicAck(message.getEnvelope().getDeliveryTag(),false);
            System.out.println("--------消息处理完成");
        };
        CancelCallback cancelCallback = consumerTag -> {};
        //每次收 1 条，处理完之前不收下一条消息；
        c.basicQos(1);
        //开始接收消息
        c.basicConsume("task_queue",false,deliverCallback,cancelCallback);
    }
}
