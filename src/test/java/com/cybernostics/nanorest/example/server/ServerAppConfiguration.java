package com.cybernostics.nanorest.example.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cybernostics.nanorest.server.NanaRestServerConfig;

@Configuration
@ComponentScan
@Import(NanaRestServerConfig.class)
public class ServerAppConfiguration {

}
;