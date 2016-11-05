package com.sinosafe.payment.config;

/**
 * exchange交换机配置
 */
public interface RabbitmqExchange {
    /**
	 * 合同exchange
	 */
	final String CONTRACT_FANOUT = "CONTRACT_FANOUT";
	final String CONTRACT_TOPIC = "CONTRACT_TOPIC";
	final String CONTRACT_DIRECT = "CONTRACT_DIRECT";
}

/**
 * queue队列配置
 */
 interface RabbitmqQueue {
	final String CONTRACE_SELF ="CONTRACT_SELF";
	final String CONTRACE_TENANT ="CONTRACT_TENANT";
}