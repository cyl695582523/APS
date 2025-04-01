package com.dksh.hkbcf;

import com.dksh.hkbcf.boss.client.BOSSClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSAuthClient;
import com.dksh.hkbcf.cpvacs.client.CPVACSServiceClient;
import com.dksh.hkbcf.client.ICKClient;
import com.dksh.hkbcf.mps.client.MPSClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Feign clients.
 * Enables Feign clients for various services:
 * - CPVACS Service Client
 * - CPVACS Auth Client
 * - BOSS Client
 * - ICK Client
 * - MPS Client
 */
@Configuration
//@EnableFeignClients(basePackages="com.dksh.hkbcf.*.client")
//@EnableFeignClients(basePackages="com.dksh.hkbcf")
@EnableFeignClients(clients = {CPVACSServiceClient.class, CPVACSAuthClient.class, BOSSClient.class, ICKClient.class, MPSClient.class})
public class FeignClientConfiguration {
}
