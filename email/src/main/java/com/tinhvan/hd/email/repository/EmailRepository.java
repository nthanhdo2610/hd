package com.tinhvan.hd.email.repository;

import com.tinhvan.hd.email.model.Email;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<Email,Long> {
}
