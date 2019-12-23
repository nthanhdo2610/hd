package com.tinhvan.hd.customer.service.impl;

import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.InternalServerErrorException;
import com.tinhvan.hd.customer.dao.CustomerImageDAO;
import com.tinhvan.hd.customer.model.CustomerImage;
import com.tinhvan.hd.customer.repository.CustomerImageRepository;
import com.tinhvan.hd.customer.service.CustomerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
//@Transactional(rollbackFor = InternalServerErrorException.class)
public class CustomerImageServiceImpl implements CustomerImageService {
    @Autowired
    private CustomerImageDAO customerImageDAO;

    @Autowired
    private CustomerImageRepository customerImageRepository;

    @Override
    public CustomerImage insert(CustomerImage image) {
        CustomerImage customerImage = customerImageDAO.findByType(image.getUuid(),image.getType());

        if (customerImage != null) {
            customerImage.setActive(HDConstant.STATUS.DISABLE);
            customerImage.setModifiedAt(image.getCreatedAt());
            customerImageRepository.save(customerImage);
        }
        //customerImageDAO.delete(image.getUuid(), image.getCreatedAt(), image.getType());

        return customerImageRepository.save(image);
    }

    @Override
    public void update(CustomerImage image) {
        customerImageRepository.save(image);
    }

    @Override
    public CustomerImage find(UUID uuid, String fileName, int type) {
        return customerImageDAO.find(uuid, fileName, type);
    }
    @Override
    public CustomerImage findByType(UUID uuid, int type) {
        return customerImageDAO.findByType(uuid, type);
    }
}
