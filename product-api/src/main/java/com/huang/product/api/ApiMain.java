package com.huang.product.api;

import com.huang.product.service.IProductService;
import com.huang.product.service.bean.Product;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author simple_huang@foxmail.com on 2018/1/5.
 */
public class ApiMain {
    public static void main(String[] args) {
        IProductService productService = (IProductService) rpc(IProductService.class);
        Product product = productService.getById(1);
        System.out.println(product);
    }

    public static Object rpc(final Class clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = new Socket("127.0.0.1", 8888);

                        String apiClassName = clazz.getName();
                        String methodName = method.getName();
                        Class[] parameterType = method.getParameterTypes();

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeUTF(apiClassName);
                        objectOutputStream.writeUTF(methodName);
                        objectOutputStream.writeObject(parameterType);
                        objectOutputStream.writeObject(args);
                        objectOutputStream.flush();

                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        Object o = objectInputStream.readObject();
                        objectInputStream.close();
                        objectOutputStream.close();

                        socket.close();
                        return o;
                    }
                });
    }

}
