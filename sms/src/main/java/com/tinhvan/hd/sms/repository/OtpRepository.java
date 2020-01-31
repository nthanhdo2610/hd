package com.tinhvan.hd.sms.repository;

import com.tinhvan.hd.sms.model.OTP;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends CrudRepository<OTP,Long> {
    @Query("FROM OTP WHERE uuid = :uuid")
    Optional<OTP> findByUuid(UUID uuid);

    List<OTP> findAllByOtpCodeAndStatusInOrderByCreatedAtDesc(String otpCode,List<Integer> status);

    @Procedure(name = "p_otp_count_by_fcm_token")
    String outputStatus(@Param("fcm_token") String fcmToken,@Param("phone") String phone);
}
