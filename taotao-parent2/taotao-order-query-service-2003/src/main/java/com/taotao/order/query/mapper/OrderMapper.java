package com.taotao.order.query.mapper;



import com.taotao.order.query.bean.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface OrderMapper extends IMapper<Order>{
	
	public void paymentOrderScan(@Param("date") Date date);

}
