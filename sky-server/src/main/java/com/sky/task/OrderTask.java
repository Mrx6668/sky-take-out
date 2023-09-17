package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    //处理超时订单的方法
    @Scheduled(cron="0 * * * * ?")  //每分钟触发一次
    public void processTimeoutOrder(){
        log.info("处理超时订单的方法：{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if (ordersList != null && !ordersList.isEmpty()){
            ordersList.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("超时未支付");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            });
        }

    }

    //每天凌晨2点处理派送中的订单
    @Scheduled(cron="0 0 2 * * ?")
    public void processDeliver(){
        log.info("每天凌晨2点处理派送中的订单:{}", LocalDateTime.now());
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusHours(2));
        if (ordersList != null && !ordersList.isEmpty()){
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            });
        }
    }
}
