package com.tinhvan.hd.sms.repository;

import com.tinhvan.hd.sms.model.OTP;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends CrudRepository<OTP,Long> {
    @Query("FROM OTP WHERE uuid = :uuid")
    Optional<OTP> findByUuid(UUID uuid);
}
