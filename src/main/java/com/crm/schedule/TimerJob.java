package com.crm.schedule;

import com.crm.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TimerJob {
    private final ProductService productService;

    @Scheduled(fixedRate = 1000 * 60)
    public void batchUpdateState() {
        System.out.println("==========定时任务执行==========");
        productService.batchUpdateProductState();
    }
}