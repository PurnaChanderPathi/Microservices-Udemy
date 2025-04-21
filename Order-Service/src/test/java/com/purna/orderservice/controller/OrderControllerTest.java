package com.purna.orderservice.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.purna.orderservice.OrderServiceConfig;
import com.purna.orderservice.repository.OrderRepository;
import com.purna.orderservice.service.OrderService;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest({"server.port=0"})
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {OrderServiceConfig.class})
 public class  OrderControllerTest {

 @Autowired
 private OrderService orderService;

 @Autowired
 private OrderRepository orderRepository;

 @Autowired
 private MockMvc mockMvc;

 @RegisterExtension
 static WireMockExtension wireMockExtension =
         WireMockExtension.newInstance()
                 .options(WireMockConfiguration.wireMockConfig().port(808))
                 .build();

 private ObjectMapper objectMapper =
         new ObjectMapper()
                 .findAndRegisterModules()
                 .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS,false)
                 .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
}