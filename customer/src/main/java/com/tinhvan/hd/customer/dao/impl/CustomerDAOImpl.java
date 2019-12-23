package com.tinhvan.hd.customer.dao.impl;

import com.tinhvan.hd.base.DAO;
import com.tinhvan.hd.base.HDConstant;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.customer.dao.CustomerDAO;
import com.tinhvan.hd.customer.model.Customer;
import com.tinhvan.hd.customer.payload.CustomerFilter;
import com.tinhvan.hd.customer.payload.StatisticsRegisterByDaysResponse;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public void insert(Customer customer) {
//
//        DAO.query((entityManager) -> {
//            entityManager.persist(customer);
//        });
//    }
//
//    @Override
//    public void update(Customer customer) {
//        DAO.query((entityManager) -> {
//            entityManager.merge(customer);
//        });
//    }
//
//    @Override
//    public Customer findByUsername(String username) {
//        List<Customer> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//            Query query = entityManager.createQuery("from Customer where username = :username");
//            query.setParameter("username", username);
//            lst.addAll(query.getResultList());
//        });
//        if (!lst.isEmpty())
//            return lst.get(0);
//        return null;
//    }
//
//    @Override
//    public Customer findByEmail(String email) {
//        if (HDUtil.isNullOrEmpty(email))
//            return null;
//        List<Customer> lst = new ArrayList<>();
//        DAO.query((entityManager) -> {
//            Query query = entityManager.createQuery("from Customer where email = :email");
//            query.setParameter("email", email);
//            lst.addAll(query.getResultList());
//        });
//        if (!lst.isEmpty())
//            return lst.get(0);
//        return null;
//    }

    @Override
    public Customer findByUuid(UUID uuid, int status) {
        List<Customer> lst = new ArrayList<>();
//        DAO.query((em) -> {
//
//        });
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("from Customer where uuid = :uuid");
        if (status >= 0) {
            joiner.add("and status = :status");
        }
        Query query = entityManager.createQuery(joiner.toString());
        query.setParameter("uuid", uuid);
        if (status >= 0) {
            query.setParameter("status", status);
        }

        lst.addAll(query.getResultList());
        if (!lst.isEmpty())
            return lst.get(0);
        return null;
    }

    @Override
    public List<Customer> find(List<UUID> customerIds, int pageNum, int pageSize, String oderBy, String direction) {

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("from Customer where (status =" + HDConstant.STATUS.DISABLE + " or status =" + HDConstant.STATUS.ENABLE + ")");
        if (customerIds != null && customerIds.size() > 0) {
            joiner.add("and uuid in (:customerIds)");
        }
        OderByAndSort(joiner, oderBy, direction);
        //System.out.println(joiner.toString());
        List<Customer> lst = new ArrayList<>();
//        DAO.query((em) -> {
//
//        });
        Query query = entityManager.createQuery(joiner.toString());
        if (customerIds != null && customerIds.size() > 0) {
            query.setParameter("customerIds", customerIds);
        }
        query.setFirstResult((pageNum - 1) * pageSize);
        query.setMaxResults(pageSize);
        lst.addAll(query.getResultList());
        return lst;

    }

    @Override
    public int count(List<UUID> customerIds) {
        List resultList = new ArrayList();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("select count(*) from Customer where (status =" + HDConstant.STATUS.DISABLE + " or status =" + HDConstant.STATUS.ENABLE + ")");
        if (customerIds != null && customerIds.size() > 0) {
            joiner.add("and uuid in (:customerIds)");
        }
//        DAO.query((entityManager) -> {
//
//        });
        Query query = entityManager.createQuery(joiner.toString());
        if (customerIds != null && customerIds.size() > 0) {
            query.setParameter("customerIds", customerIds);
        }
        resultList.addAll(query.getResultList());
        if (!resultList.isEmpty())
            return Integer.parseInt(String.valueOf(resultList.get(0)));
        return 0;
    }

//    @Override
//    public Customer findByPhoneNumber(String phoneNumber) {
//        List<Customer> resultList = new ArrayList<>();
//        DAO.query((entityManager) -> {
//            Query query = entityManager.createQuery("from Customer where phoneNumber = :phoneNumber");
//            query.setParameter("phoneNumber", phoneNumber);
//            resultList.addAll(query.getResultList());
//        });
//        if (!resultList.isEmpty())
//            return resultList.get(0);
//        return null;
//    }

    @Override
    public List<Customer> findCustomerIdByFullNameOrEmail(String info, int pageNum, int pageSize, String oderBy, String direction) {
        List<Customer> lst = new ArrayList<>();

        if (!HDUtil.isNullOrEmpty(info)) {
            String[] key = info.split(",");
//            DAO.query((entityManager) -> {
//
//            });
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add("from Customer where 1=2");
            for (int i = 0; i < key.length; i++) {
                String s = key[i].trim().toUpperCase();
                joiner.add("or upper(fullName) like '%" + s + "%' or upper(email) like '%" + s + "%'");
            }
            OderByAndSort(joiner, oderBy, direction);
            System.out.println("joiner.toString():" + joiner.toString());
            Query query = entityManager.createQuery(joiner.toString());
            query.setFirstResult((pageNum - 1) * pageSize);
            query.setMaxResults(pageSize);
            lst.addAll(query.getResultList());
        }
        return lst;
    }

    @Override
    public int countCustomerIdByFullNameOrEmail(String info) {
        List<String> lst = new ArrayList<>();
        if (!HDUtil.isNullOrEmpty(info)) {
            String[] key = info.split(",");
//            DAO.query((entityManager) -> {
//
//            });
            StringJoiner joiner = new StringJoiner(" ");
            joiner.add("select count(*) from Customer where 1=2");
            for (int i = 0; i < key.length; i++) {
                String s = key[i].trim().toUpperCase();
                joiner.add("or upper(fullName) like '%" + s + "%' or upper(email) like '%" + s + "%'");
            }
            Query query = entityManager.createQuery(joiner.toString());
            lst.addAll(query.getResultList());
        }
        if (!lst.isEmpty())
            return Integer.parseInt(String.valueOf(lst.get(0)));
        return 0;
    }

    @Override
    public int countRegister(int status) {
        List<String> lst = new ArrayList<>();

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("select count(*) from Customer");
        if (status >= 0)
            joiner.add("where status=:status");
        else
            joiner.add("where status=0 or status=1");
        Query query = entityManager.createQuery(joiner.toString());
        if (status >= 0)
            query.setParameter("status", status);
        lst.addAll(query.getResultList());
        return Integer.parseInt(String.valueOf(lst.get(0)));
    }

    @Override
    public List<StatisticsRegisterByDaysResponse> statisticsRegisterByDays(int numOfDays) {
        List<StatisticsRegisterByDaysResponse> results = new ArrayList<>();

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("select TO_CHAR(created_at, 'dd/MM/yyyy') date,");
        joiner.add("count(*) count");
        joiner.add("from customer where status <>-1");
        joiner.add("and created_at  + (" + numOfDays + " * interval '1 day') >= date_trunc('day',now())");
        joiner.add("group by date");
        joiner.add("order by date desc");
        Query query = entityManager.createNativeQuery(joiner.toString());
        List<Object[]> lst = query.getResultList();
        results.addAll(lst.stream().map(result -> new StatisticsRegisterByDaysResponse(
                (String) result[0], ((BigInteger) result[1]).intValue()
        )).collect(Collectors.toList()));
        return results;
    }

    /**
     * function used
     */
    void OderByAndSort(StringJoiner joiner, String oderBy, String direction) {

        String[] oder = oderBy.split(",");
        String[] direct = direction.split(",");

        joiner.add("order by");
        if (HDUtil.isNullOrEmpty(oderBy)) {
            joiner.add("createdAt desc");
        } else {
            for (int i = 0; i < oder.length; i++) {
                if (!HDUtil.isNullOrEmpty(oder[i])) {
                    joiner.add(oder[i]);
                }
                if (!HDUtil.isNullOrEmpty(direct[i])) {
                    joiner.add(direct[i]);
                }
                if (i < oder.length - 1)
                    joiner.add(",");
            }
        }
    }

    void generalFilter(CustomerFilter filter, StringJoiner joiner) {
        if (filter != null) {
            if (filter.getAgeFrom() != null && filter.getAgeFrom() > 0) {
                joiner.add("and date_part('year',age(birthdate)) >= :ageFrom");
            }
            if (filter.getAgeTo() != null && filter.getAgeTo() > 0) {
                joiner.add("and date_part('year',age(birthdate)) <= :ageTo");
            }
            if (filter.getGender() != null && filter.getGender() > 0) {
                joiner.add("and gender = :gender");

            }
            if (filter.getProvince() != null) {
                joiner.add("and province = :province");
            }
            if (filter.getDistrict() != null) {
                joiner.add("and district = :district");
            }
        }
    }

    void setFilter(CustomerFilter filter, Query query) {
        if (query != null && filter != null) {
            if (filter.getAgeFrom() != null && filter.getAgeFrom() > 0) {
                query.setParameter("ageFrom", filter.getAgeFrom());
            }
            if (filter.getAgeTo() != null && filter.getAgeTo() > 0) {
                query.setParameter("ageTo", filter.getAgeTo());
            }
            if (filter.getGender() != null && filter.getGender() > 0) {
                query.setParameter("gender", filter.getGender());
            }
            if (filter.getProvince() != null) {
                query.setParameter("province", filter.getProvince());
            }
            if (filter.getDistrict() != null) {
                query.setParameter("district", filter.getDistrict());
            }
        }
    }

}
