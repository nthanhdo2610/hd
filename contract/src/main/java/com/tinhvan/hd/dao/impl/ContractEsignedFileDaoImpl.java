package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.dao.ContractEsignedFileDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Repository
public class ContractEsignedFileDaoImpl implements ContractEsignedFileDao {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public void create(ContractEsignedFile contractEsignedFile) {
//        DAO.query((em) -> {
//            em.persist(contractEsignedFile);
//        });
//    }
//
//    @Override
//    public void update(ContractEsignedFile contractEsignedFile) {
//        DAO.query((em) -> {
//            em.merge(contractEsignedFile);
//        });
//    }
//
//    @Override
//    public void delete(ContractEsignedFile contractEsignedFile) {
//        DAO.query((em) -> {
//            em.remove(contractEsignedFile);
//        });
//    }
//
//    @Override
//    public ContractEsignedFile getById(Integer id) {
//        List<ContractEsignedFile> ls = new ArrayList<>();
//        DAO.query((em) -> {
//            ls.add(em.find(ContractEsignedFile.class, id));
//
//        });
//        return ls.get(0);
//    }

    @Override
    public List<String> getFile(UUID contractUuid, String fileType) {
        List<String> ls;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select fileName from ContractEsignedFile where contractId= :contractUuid ");
        if(!HDUtil.isNullOrEmpty(fileType))
            queryBuilder.append("and contractFileType= :fileType");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("contractUuid", contractUuid);
        if(!HDUtil.isNullOrEmpty(fileType))
            query.setParameter("fileType", fileType);
        ls = query.getResultList();
        return ls;
    }
}
