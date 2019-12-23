package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.config.RabbitConfig;
import com.tinhvan.hd.dao.ContractDao;
import com.tinhvan.hd.dto.*;
import com.tinhvan.hd.entity.Contract;
import com.tinhvan.hd.repository.ContractRepository;
import com.tinhvan.hd.service.ContractService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author tuongnk on 7/9/2019
 * @project notification
 */
@Service
public class ContractServiceImpl implements ContractService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ContractServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractRepository contractRepository;


    @Override
    public void createContract(Contract contract) {
        contractRepository.save(contract);
    }

    @Override
    public void updateContract(Contract contract) {
        contractRepository.save(contract);
    }

    @Override
    public void deleteContract(Contract contract) {
        contractRepository.delete(contract);
    }

    @Override
    public Contract getById(UUID id) {
        return contractRepository.getContractByContractUuid(id);
    }

    @Override
    public Contract getContractByContractCode(String contractCode) {
        return contractRepository.getContractByLendingCoreContractId(contractCode);
    }


    @Override
    public List<Contract> getListContractByFilter(ContractFilter contractFilter) {
        return contractDao.getListContractByFilter(contractFilter);
    }

    @Override
    public List<Contract> getListContractByIdentifyId(String identifyId) {
        return contractRepository.findAllByIdentifyId(identifyId);
    }

    @Override
    public List<Contract> getListContractByContractCodeOrIdentifyId(CustomerFilter customerFilter) {
        return contractDao.getListContractByContractCodeOrIdentifyId(customerFilter);
    }

    @Override
    public List<Contract> getContractByContractCodes(List<String> contractCodes) {
        return contractRepository.findAllByLendingCoreContractIdIn(contractCodes);
    }

    @Override
    public List<Contract> searchContractByContractCode(String contractCode, Pageable pageable) {
        Page<Contract> pagedResult = contractRepository.findByLendingCoreContractIdContaining(contractCode, null);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Contract>();
        }
    }

    @Override
    public List<Contract> searchContractByContractCodeAndPageable(String contractCode, Pageable pageable) {
        Page<Contract> pagedResult = contractRepository.findByLendingCoreContractIdContaining(contractCode, pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Contract>();
        }
    }

    @Override
    public List<Contract> getListContractInfo(Pageable pageable) {
        Page<Contract> pagedResult = contractRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Contract>();
        }
    }

    @Override
    public int countContract() {
        return (int) contractRepository.count();
    }

    @Override
    public int countContractByContractCode(String contractCode) {
        return contractRepository.countAllByLendingCoreContractIdContaining(contractCode);
    }

    @Override
    public List<Contract> getListContractByStatus(List<String> status) {
        return contractRepository.findAllByStatusIn(status);
    }

    @Override
    public DashBoardContract getCountContractForDashBoard(List<String> waiting) {

        Long total = contractRepository.count();
        DashBoardContract dashBoardContract = new DashBoardContract();
        dashBoardContract.setTotalContract(total);
        return dashBoardContract;
    }

    @Override
    public List<String> getAllContractCodesLive() {
        return contractDao.getAllContractCodesLive();
    }

    @Override
    public void updateContractByCustomerUuid(UUID customerUuid) {
        try {
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_UPDATE_CONTRACT_FROM_MIDDLE, customerUuid);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateContractAll(List<Contract> contracts) {
        contractRepository.saveAll(contracts);
    }

    @Override
    public List<ContractsByCustomerUuid> getListContractByCustomerUuid(UUID customerUuid) {
        return contractDao.getListContractByCustomerUuid(customerUuid);
    }

    @Override
    public List<UpdateRepayment> getContractCodeAndStatusUpdateRepayment() {
        return contractDao.getContractCodeAndStatusUpdateRepayment();
    }
    @Override
    public List<AdjConfirmDto> getAdjConfirmByContractCode(String contractCode) {
        return contractDao.getAdjConfirmByContractCode(contractCode);
    }

    @Override
    public List<ContractWaitingForSigning> getContractWaitingForSigning(String customerUuid) {
        return contractDao.getContractWaitingForSigning(customerUuid);
    }

    @Override
    public List<Contract> getContractListCurrentContract(List<String> status) {
        return contractRepository.findAllByStatusIn(status);
    }

    @Override
    public List<WaitingAdjustment> getContractAdjustmentInfoByCustomerUuid(String customerUuid) {
        return contractDao.getContractAdjustmentInfoByCustomerUuid(customerUuid);
    }

    @Override
    public List<PopupNotification> getListContractDuePayment(UUID customerUuid) {
        return contractDao.getListContractDuePayment(customerUuid);
    }

    @Override
    public List<ContractByCustomer> getContractCodesByCustomer(UUID customerUuid) {
        return contractDao.getContractCodesByCustomer(customerUuid);
    }

    @Override
    public int countContractWaitingEsign() {
        return contractDao.countContractWaitingEsign();
    }

    @Override
    public List<ObjectSendMailIT> getListAdjustmentInfoSendMailIt() {
        return contractDao.getListAdjustmentInfoSendMailIt();
    }

    @Override
    public void autoAlertRepaymentNotification() {
        contractDao.autoAlertRepaymentNotification();
    }

    @Override
    public List<String> getContractByCustomerActive(String contractCode,PageSearch pageSearch) {
        return contractDao.getContractByCustomerActive(contractCode,pageSearch);
    }
}
