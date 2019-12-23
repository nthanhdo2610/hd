//package com.tinhvan.hd.controller;
//
//import com.tinhvan.hd.base.HDController;
//import com.tinhvan.hd.base.RequestDTO;
//import com.tinhvan.hd.dto.ContractRepaymentRequest;
//import com.tinhvan.hd.entity.ContractRepayment;
//import com.tinhvan.hd.service.ContractRepaymentService;
//import com.tinhvan.hd.utils.ContractUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/contract/contract_repayment")
//public class ContractRepaymentController extends HDController {
//    @Autowired
//    private ContractRepaymentService contractRepaymentService;
//
//
//    @PostMapping("/create")
//    public ResponseEntity<?> create(@RequestBody RequestDTO<ContractRepaymentRequest> req){
//        ContractRepaymentRequest request = req.init();
////        ContractRepayment repayment = new ContractRepayment();
////        repayment.setContractUuid(UUID.fromString(request.getContractUuid()));
////        repayment.setCreateTime(req.now());
////        repayment.setPaidDate(request.getPaidDate());
////        repayment.setPayment(request.getPayment());
////        repayment.setStatus(request.getStatus());
//        //.create(repayment);
//        return ok(null);
//    }
//}
