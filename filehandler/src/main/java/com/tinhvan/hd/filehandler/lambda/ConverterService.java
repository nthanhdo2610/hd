package com.tinhvan.hd.filehandler.lambda;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConverterService {

    private InvokeRequest invokeRequest = new InvokeRequest()
            .withFunctionName("arn:aws:lambda:ap-southeast-1:996891129323:function:convert-to-pdf");
            //.withFunctionName("arn:aws:lambda:ap-southeast-1:212231711180:function:test");
    private BasicAWSCredentials credentials = new
            BasicAWSCredentials("AKIA6QG2XOXVXFFMAJGV", "ALWsdl17Frt90NbbZ9qgI1v+mHb5zhuDMvv4T0hN");
            //BasicAWSCredentials("AKIAIU3P7V47JQ65OPLA", "0JhIWkmi0TO8JqgBfYW6JSndi/tdokOM1Vxm69sO");

    private AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.AP_SOUTHEAST_1).build();

    public String lambdaConvert(String filename) {
        StopWatch sw = new StopWatch();
        sw.start();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("filename", filename);
        InvokeResult invokeResult = null;
        try {
            invokeRequest.withPayload(jsonObject.toString());
            invokeResult = awsLambda.invoke(invokeRequest);

            String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
            System.out.println(ans);
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        sw.stop();
        System.out.println(sw.getTotalTimeMillis());
        return null;
    }
}
