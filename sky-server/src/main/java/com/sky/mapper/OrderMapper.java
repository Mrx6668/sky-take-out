package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.vo.OrderVO;
import com.sky.vo.SalesTop10;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Select("select * from orders where id = #{id}")
    OrderVO getById(Long id);

    @Select("select * from orders where id=#{id}")
    OrderVO getOrderDetail(Long id);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

//    @AutoFill(value = OperationType.INSERT)
    void addShoppingCarts(ArrayList<ShoppingCart> shoppingCarts);

    @Select("select count(id) from orders where status = #{status}")
    Integer selectByStatus(Integer status);

    @Update("update orders set status = #{status} where id = #{id}")
    void setStatus(Long id, Integer status);

    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime time);

   Double getOrderByTime(LocalDateTime beginTime, LocalDateTime endTime);

    Integer getOrderReport(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

    @Results({
            @Result(column = "name", property = "name"),
            @Result(column = "sum(number)",property = "number")
    })
    List<GoodsSalesDTO> getTop10(LocalDateTime beginTimes, LocalDateTime endTimes);
}
