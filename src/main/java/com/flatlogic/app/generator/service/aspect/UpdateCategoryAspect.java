package com.flatlogic.app.generator.service.aspect;

import com.flatlogic.app.generator.repository.OrdersRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class UpdateCategoryAspect {

    @Autowired
    private OrderRepository orderRepository;

    @After("execution(* com.flatlogic.app.generator.repository.ProductRepository.updateDeletedAt(..))")
    public void setProductIdAtNull(final JoinPoint joinPoint) {
        orderRepository.setProductIdAtNull((UUID) joinPoint.getArgs()[0]);
    }

    @After("execution(* com.flatlogic.app.generator.repository.UserRepository.updateDeletedAt(..))")
    public void setUserIdAtNull(final JoinPoint joinPoint) {
        orderRepository.setUserIdAtNull((UUID) joinPoint.getArgs()[0]);
    }

}
