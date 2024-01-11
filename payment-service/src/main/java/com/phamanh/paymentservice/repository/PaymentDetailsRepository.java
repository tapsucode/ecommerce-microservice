package com.phamanh.paymentservice.repository;

import com.phamanh.paymentservice.domains.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    public  PaymentDetails findByApp_Trans_Id(String app_trans_id);

  List<PaymentDetails> findByStatus(PaymentDetails.Status status);
}
