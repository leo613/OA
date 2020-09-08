package com.taotao.common.service;

/**
 * 公用方法抽取
 */
public interface Function<T,E> {

    public T callback(E e);

}
