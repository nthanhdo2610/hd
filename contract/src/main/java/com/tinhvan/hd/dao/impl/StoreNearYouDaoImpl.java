package com.tinhvan.hd.dao.impl;

import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.dao.StoreNearYouDao;
import com.tinhvan.hd.dto.PageSearch;
import com.tinhvan.hd.dto.SearchStore;
import com.tinhvan.hd.entity.StoreNearYou;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class StoreNearYouDaoImpl implements StoreNearYouDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<StoreNearYou> getAllStoreByFilter(SearchStore searchStore, Integer status) {
//        PageSearch pageSearch = searchStore.getPages();
//        int page = 1;
//        int pageSize = 20;
//        if (pageSearch != null) {
//            page = pageSearch.getPage();
//            pageSize = pageSearch.getPageSize();
//        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("From StoreNearYou where 1 = 1 ");
        generateFilter(searchStore,queryBuilder);

        Query query = entityManager.createQuery(queryBuilder.toString());
//        query.setFirstResult((page - 1) * pageSize);
//        query.setMaxResults(pageSize);
        setFilter(searchStore,query);

        return query.getResultList();
    }

    public void generateFilter(SearchStore searchStore, StringBuilder stringBuilder){

        if (searchStore != null) {
            if (!HDUtil.isNullOrEmpty(searchStore.getProvinceCode())){
                stringBuilder.append(" and provinceCode = :provinceCode");
            }

            if(!HDUtil.isNullOrEmpty(searchStore.getDistrictCode())){
                stringBuilder.append(" and districtCode= :districtCode ");
            }
        }
    }

    public void setFilter(SearchStore searchStore, Query query){
        if (searchStore != null){
            if (!HDUtil.isNullOrEmpty(searchStore.getProvinceCode())){
                query.setParameter("provinceCode",searchStore.getProvinceCode());
            }

            if(!HDUtil.isNullOrEmpty(searchStore.getDistrictCode())){
                query.setParameter("districtCode",searchStore.getDistrictCode());
            }
        }
    }
}
