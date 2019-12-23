package com.tinhvan.hd.service.impl;

import com.tinhvan.hd.dao.SchemeDao;
import com.tinhvan.hd.entity.Scheme;
import com.tinhvan.hd.repository.SchemeRepository;
import com.tinhvan.hd.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class SchemeServiceImpl implements SchemeService {

    @Autowired
    private SchemeDao schemeDao;

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private EntityManager entityManager;


    @Override
    public void insertScheme(Scheme scheme) {
        schemeRepository.save(scheme);
    }

    @Override
    public void updateScheme(Scheme scheme) {
        schemeRepository.save(scheme);
    }

    @Override
    public void deleteScheme(Scheme scheme) {
        schemeRepository.delete(scheme);
    }

    @Override
    public Scheme getById(Long id) {
        return schemeRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public List<Scheme> getAll() {
        StringBuilder queryStr = new StringBuilder("SELECT a.id id, a.scheme_name schemeName, a.scheme_value schemeValue, a.file_link fileLink, \n" +
                "a.created_at createdAt, Cast(a.created_by as varchar) createdBy, s.full_name createdByName, \n" +
                "a.modified_at modifiedAt, Cast(a.modified_by as varchar) modifiedBy \n" +
                "FROM public.scheme a left join public.staff s on a.created_by = s.id where 1=1 order by a.created_at desc");
        Query query = entityManager.createNativeQuery(queryStr.toString(), "SchemeDtoMapping");
        return query.getResultList();
    }

    @Override
    public List<Scheme> findBySchemeCode(String schemeCode) {
        return schemeDao.findBySchemeCode(schemeCode);
    }
}
