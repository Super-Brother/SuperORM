package com.wenchao.superorm;

/**
 * @author wenchao
 * @date 2019/7/28.
 * @time 17:25
 * description：
 */
public interface IBaseDao<T> {

    long insert(T entity);
}
