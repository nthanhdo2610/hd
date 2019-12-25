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

import java.nio.charset.StandardCharsets;

public class ConverterService {

    public static void main(String[] args) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("filename","Ban_Dieu_Khoan_Va_Dieu_Kien_Chung.docx");
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName("arn:aws:lambda:ap-southeast-1:212231711180:function:serverlessrepo-docx-to-pdf-S3Fn-1EPAEJXO4HN30:2")
                .withPayload(jsonObject.toString());
        InvokeResult invokeResult = null;
        try {
            BasicAWSCredentials credentials = new
                    BasicAWSCredentials("AKIAJY5TWUXF6FAY3BZQ", "LkUsA9qYuewIiBXf24bdKYf0xwLHvdRgdTLo3sW+");
            AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.AP_SOUTHEAST_1).build();

            invokeResult = awsLambda.invoke(invokeRequest);

            String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);

            //write out the return value
            System.out.println(ans);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(invokeResult.getPayload());
    }
}
