package net.henanyuanhang.sms.core.interceptor;

import com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler;
import net.henanyuanhang.sms.common.utils.CollectionUtils;

import java.util.*;

/**
 * 拦截器管理器
 *
 * @author zhangzhiyong
 * @createTime 2021年11月11日 21:33
 */
public class SendInterceptorManager {

    private LinkedList<SendInterceptor> interceptors = new LinkedList<>();

    private Set<Class> classSet = new HashSet<>();

    public SendInterceptorManager() {
        addDefaultSendInterceptor();
    }

    public List<SendInterceptor> getInterceptors() {
        return new ArrayList<>(interceptors);
    }


    /**
     * 查找该类在list中的下标。
     *
     * @param tClass 查找的类型
     * @return 如果list为空，则返回0。如果该类在list中，则返回该类在list的下标值，否则返回-1。
     */
    private int indexOf(Class tClass) {
        if (interceptors.isEmpty()) {
            return 0;
        }
        int index = 0;
        for (SendInterceptor sendInterceptor : interceptors) {
            if (sendInterceptor.getClass().equals(tClass)) {
                return index;
            } else {
                index++;
            }
        }
        return -1;
    }

    /**
     * 添加到指定拦截器类之前
     *
     * @param sendInterceptor 要添加的拦截器
     * @param tClass          指定拦截器类
     * @return
     */
    public SendInterceptorManager addBefore(SendInterceptor sendInterceptor, Class<SendInterceptor> tClass) {
        checkClass(sendInterceptor.getClass());
        int index = interceptors.indexOf(tClass);
        if (index == -1) {
            throw new IllegalArgumentException("未找到该拦截器 " + tClass.getName());
        }
        addInterceptor(index, sendInterceptor);
        return this;
    }

    /**
     * 添加到指定拦截器类之后
     *
     * @param sendInterceptor 要添加的拦截器
     * @param tClass          指定拦截器类
     * @return
     */
    public SendInterceptorManager addAfter(SendInterceptor sendInterceptor, Class<SendInterceptor> tClass) {
        checkClass(sendInterceptor.getClass());
        int index = interceptors.indexOf(tClass);
        if (index == -1) {
            throw new IllegalArgumentException("未找到该拦截器 " + tClass.getName());
        }
        addInterceptor(index + 1, sendInterceptor);
        return this;
    }

    /**
     * 替换指定的拦截器
     *
     * @param sendInterceptor 要替换的拦截器
     * @param tClass          指定拦截器类
     * @return
     */
    public SendInterceptorManager addAt(SendInterceptor sendInterceptor, Class<SendInterceptor> tClass) {
        checkClass(sendInterceptor.getClass());
        int index = interceptors.indexOf(tClass);
        if (index == -1) {
            throw new IllegalArgumentException("未找到该拦截器 " + tClass.getName());
        }
        interceptors.set(index, sendInterceptor);
        classSet.add(tClass);
        classSet.remove(tClass);
        return this;
    }

    /**
     * 移除指定类型的拦截器
     *
     * @param tClass
     * @return
     */
    public SendInterceptorManager remove(Class<SendInterceptor> tClass) {
        if (CollectionUtils.notEmpty(interceptors) && classSet.remove(tClass)) {
            int index = indexOf(tClass);
            interceptors.remove(index);
        }
        return this;
    }

    /**
     * 添加到拦截器列表开头
     *
     * @param sendInterceptor
     * @return
     */
    public SendInterceptorManager addFirst(SendInterceptor sendInterceptor) {
        checkClass(sendInterceptor.getClass());
        addInterceptor(0, sendInterceptor);
        return this;
    }

    /**
     * 添加到拦截器列表末尾
     *
     * @param sendInterceptor
     * @return
     */
    public SendInterceptorManager addLast(SendInterceptor sendInterceptor) {
        checkClass(sendInterceptor.getClass());
        addInterceptor(interceptors.size(), sendInterceptor);
        return this;
    }

    private void addInterceptor(int index, SendInterceptor sendInterceptor) {
        if (index == 0) {
            interceptors.addFirst(sendInterceptor);
        } else {
            interceptors.add(index, sendInterceptor);
        }
        classSet.add(sendInterceptor.getClass());
    }

    private void checkClass(Class c) {
        if (classSet.contains(c)) {
            throw new IllegalArgumentException("该拦截器 " + c.getName() + " 已存在");
        }
    }

    private void removeClass(Class c) {
        classSet.remove(c);
    }

    /**
     * 添加默认的拦截器
     */
    private void addDefaultSendInterceptor() {
        addInterceptor(interceptors.size(), new NullParamsInterceptor());

    }
}
