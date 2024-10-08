package com.kenzie.appserver.service;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import dagger.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.beans.BeanProperty;
import java.util.Collections;

@Service
public class CloudWatchService {

    private final AmazonCloudWatch cloudWatch;


    public CloudWatchService() {
        this.cloudWatch = AmazonCloudWatchClientBuilder.defaultClient();
    }

    public void publishMetric(String metricName, double value) {
        MetricDatum datum = new MetricDatum()
                .withMetricName(metricName)
                .withUnit(StandardUnit.Count)
                .withValue(value);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("CustomMetrics")
                .withMetricData(Collections.singletonList(datum));

        PutMetricDataResult response = cloudWatch.putMetricData(request);
        System.out.println("Metric Published: " + response);
    }
}
