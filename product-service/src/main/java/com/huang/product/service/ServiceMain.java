package com.huang.product.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author simple_huang@foxmail.com on 2018/1/5.
 */
public class ServiceMain {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                //读取网络协议
                String apiClassName = objectInputStream.readUTF();
                String methodName = objectInputStream.readUTF();
                Class[] parameterTypes = (Class[]) objectInputStream.readObject();
                Object[] args4Method = (Object[]) objectInputStream.readObject();

                Class clazz = null;
                //api到具体实现的映射
                if (apiClassName.equals(IProductService.class.getName())) {
                    clazz = ProductService.class;
                }

                Method method = clazz.getMethod(methodName, parameterTypes);
                Object invoke = method.invoke(clazz.newInstance(), args4Method);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(invoke);
                objectOutputStream.flush();

                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
