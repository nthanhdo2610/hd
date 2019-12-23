/*
package com.tinhvan.hd.quartz.job;

import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.Invoker;
import com.tinhvan.hd.base.ResponseDTO;
import com.tinhvan.hd.dto.SendEmailRequest;
import com.tinhvan.hd.entity.ContractSendFile;
import com.tinhvan.hd.service.ContractEsignedFileService;
import com.tinhvan.hd.service.ContractSendFileService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@DisallowConcurrentExecution
public class ContractSendFileJob implements Job {

    @Autowired
    private ContractSendFileService contractSendFileService;

    @Autowired
    private ContractEsignedFileService eSignedFileService;

    @Value("${service.email.endpoint}")
    private String urlEmailRequest;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {*/
/*
        List<ContractSendFile> contractSendFiles = contractSendFileService.findSend();
        if (contractSendFiles != null && contractSendFiles.size() > 0) {
            System.out.println("ContractSendFileJob:" + contractSendFiles.size());
            Invoker invoker = new Invoker();
            for (ContractSendFile contractSendFile : contractSendFiles) {
                List<String> attachments = eSignedFileService.getFile(contractSendFile.getContractUuid());
                SendEmailRequest emailRequest = new SendEmailRequest();
                emailRequest.setListEmail(Arrays.asList(contractSendFile.getEmail()));
                emailRequest.setListFile(attachments);
                emailRequest.setSubject("Test Send Email ContractSendFileJob");
                emailRequest.setContent("Content is empty because this is example");
                ResponseDTO<Object> dto = invoker.call(urlEmailRequest + "/send_s3", emailRequest,
                        new ParameterizedTypeReference<ResponseDTO<Object>>() {
                        });
                System.out.println(dto.getCode());
                if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
                    contractSendFile.setStatus(1);
                    contractSendFile.setFileName(attachments.toString());
                    contractSendFileService.update(contractSendFile);
                } else {
                    break;
                }
            }
        }
   *//*
 }
}
*/
