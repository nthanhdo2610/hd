package com.tinhvan.hd.components;

import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.dto.HDContractResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ContractListener {


    @RabbitListener(queues = RabbitConfig.QUEUE_CONTRACT)
    public void handlingContract(HDContractResponse hdContractResponse) {

    }

}
