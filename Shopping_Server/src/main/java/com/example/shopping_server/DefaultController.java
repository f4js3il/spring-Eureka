package com.example.shopping_server;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(path="/shopping-server")
public class DefaultController {

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private RestTemplate restTemplate;



    @GetMapping("/**")
    public <T> ResponseEntity<T> getResponse(HttpServletRequest httpServletRequest) {
        List<String> serverDetails = Arrays.asList(httpServletRequest.getRequestURI().split("shopping-server")[1].split("/"));
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        AtomicLong instanceId = registry.getApplications().getNextIndex(serverDetails.get(1).toUpperCase(), false);
        Application application = registry.getApplication(serverDetails.get(1).toUpperCase());
        InstanceInfo instanceInfo = application.getInstances().get(Integer.parseInt(String.valueOf(instanceId)));
        List<String> uriComponents =  serverDetails.subList(2,serverDetails.size());
        String urlString = String.join("/",uriComponents);
        return (ResponseEntity<T>) restTemplate.getForEntity(instanceInfo.getHomePageUrl()+urlString, String.class);
    }
}
