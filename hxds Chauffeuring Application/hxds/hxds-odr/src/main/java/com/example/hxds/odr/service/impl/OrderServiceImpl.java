package com.example.hxds.odr.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.odr.db.dao.OrderBillDao;
import com.example.hxds.odr.db.dao.OrderDao;
import com.example.hxds.odr.db.pojo.OrderBillEntity;
import com.example.hxds.odr.db.pojo.OrderEntity;
import com.example.hxds.odr.service.OrderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderBillDao orderBillDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public HashMap searchDriverTodayBusinessData(long driverId) {
        return orderDao.searchDriverTodayBusinessData(driverId);
    }

    @Override
    @Transactional
    @LcnTransaction
    public String insertOrder(OrderEntity orderEntity, OrderBillEntity billEntity) {
        //插入订单记录
        int rows = orderDao.insert(orderEntity);
        if (rows == 1) {
            String id = orderDao.searchOrderIdByUUID(orderEntity.getUuid());
            //插入订单费用记录
            billEntity.setOrderId(Long.parseLong(id));
            rows = orderBillDao.insert(billEntity);
            if (rows == 1) {
                //往Redis里面插入缓存，配合Redis事务用于司机抢单，避免多个司机同时抢单成功
                redisTemplate.opsForValue().set("order#" + id, "none");
                redisTemplate.expire("order#" + id, 15, TimeUnit.MINUTES);   //缓存15分钟
                return id;
            } else {
                throw new HxdsException("保存新订单费用失败");
            }
        } else {
            throw new HxdsException("保存新订单失败");
        }
    }
}
