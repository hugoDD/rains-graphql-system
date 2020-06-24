package com.rains.graphql.arthas.controller;

import com.rains.graphql.arthas.entity.ArthasMachineinfo;
import com.rains.graphql.arthas.service.ArthasMachineinfoService;
import com.rains.graphql.monitor.event.InstanceEndpointsDetectedEvent;
import com.rains.graphql.monitor.event.InstanceEventLog;
import com.rains.graphql.monitor.event.InstanceRegisteredEvent;
import com.rains.graphql.monitor.event.InstanceStatusChangedEvent;
import com.rains.graphql.monitor.model.Application;
import com.rains.graphql.monitor.model.Endpoints;
import com.rains.graphql.monitor.model.Instance;
import com.rains.graphql.monitor.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.boot.actuate.endpoint.web.PathMappedEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/devops"})
public class ApplicationCtrl {

    @Autowired
    private List<? extends ExposableEndpoint<?>> endpoints;

    @Autowired
    private Application application;

    @Autowired
    private InstanceEventLog instanceEventLog;

    @Autowired
    private ArthasMachineinfoService arthasMachineinfoService;

    @GetMapping(path = {"/applications"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Application> applications(HttpServletRequest request, HttpServletResponse response) {
        List<ArthasMachineinfo> machineList = arthasMachineinfoService.list();
        List<Application> apps = machineList.stream().map(m->{
            Application application = new Application();
            application.setName("machinelId:"+m.getId());
            initApplicationInfo(request,application);
            return application;
        }).collect(Collectors.toList());

        return apps;
    }

    /**
     * 组装application信息
     *
     * @param request
     */
    public void initApplicationInfo(HttpServletRequest request,Application application) {
        String normalizedUrl = normalizeRequestUrl(request);
        Map<String, Link> links = new LinkedHashMap<>();
        for (ExposableEndpoint<?> endpoint : this.endpoints) {
            if (endpoint instanceof ExposableWebEndpoint) {
                collectLinks(links, (ExposableWebEndpoint) endpoint, normalizedUrl);
            } else if (endpoint instanceof PathMappedEndpoint) {
                links.put(endpoint.getEndpointId().toString(), createLink(normalizedUrl, ((PathMappedEndpoint) endpoint).getRootPath()));
            }
        }
        Endpoints endpoints = new Endpoints(links);
        Registration registration = new Registration(application.getName(), normalizedUrl, normalizedUrl + "/health",
                hostUrl(request), "http-api");
        Instance instance = new Instance(application.getName(), registration, endpoints);
        application.setInstances(Arrays.asList(instance));

        if (!instanceEventLog.isHasInit()) {
            InstanceRegisteredEvent instanceRegisteredEvent = new InstanceRegisteredEvent(application.getName(), 0,
                    Instant.now(), registration);
            instanceEventLog.add(instanceRegisteredEvent);
            InstanceStatusChangedEvent instanceStatusChangedEvent = new InstanceStatusChangedEvent(
                    application.getName(), 1, Instant.now(), instance.getStatusInfo());
            instanceEventLog.add(instanceStatusChangedEvent);
            InstanceEndpointsDetectedEvent instanceEndpointsDetectedEvent = new InstanceEndpointsDetectedEvent(
                    application.getName(), 2, Instant.now(), endpoints);
            instanceEventLog.add(instanceEndpointsDetectedEvent);
            instanceEventLog.setHasInit(true);
        }
    }


    private String normalizeRequestUrl(HttpServletRequest request) {
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();

        if (!uri.endsWith("/")) {
            uri = uri + "/";
        }

        return uri + "actuator";
    }

    private String hostUrl(HttpServletRequest request) {
        String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();

        if (!uri.endsWith("/")) {
            uri = uri + "/";
        }

        return uri;
    }

    private void collectLinks(Map<String, Link> links, ExposableWebEndpoint endpoint, String normalizedUrl) {
        for (WebOperation operation : endpoint.getOperations()) {
            links.put(operation.getId(), createLink(normalizedUrl, operation));
        }
    }

    private Link createLink(String requestUrl, WebOperation operation) {
        return createLink(requestUrl, operation.getRequestPredicate().getPath());
    }

    private Link createLink(String requestUrl, String path) {
        return new Link(requestUrl + (path.startsWith("/") ? path : "/" + path));
    }

}
