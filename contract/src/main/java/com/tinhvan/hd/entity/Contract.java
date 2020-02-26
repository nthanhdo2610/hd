package com.tinhvan.hd.entity;

import com.tinhvan.hd.base.HDPayload;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CONTRACT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SequenceGenerator(name = "SEQ_STORE", sequenceName = "CONTRACT_SEQ", allocationSize = 1)
public class Contract implements HDPayload,Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "ID",length = 64)
    private Integer id;

    @Basic
    @Column(name = "CONTRACT_UUID")
    private UUID contractUuid;

    @Basic
    @Column(name = "LENDINGCORE_CONTRACT_ID",length = 128)
    private String lendingCoreContractId;

    @Basic
    @Column(name = "PHONE",length = 20)
    private String phone;

    @Basic
    @Column(name = "IDENTIFY_ID",length = 20)
    private String identifyId;


    @Basic
    @Column(name = "STATUS")
    private String status;

    @Basic
    @Column(name = "IS_SIGN_UP",columnDefinition = "SMALLINT")
    private Integer isSignUp;

    @Basic
    @Column(name = "IS_REMOVE",columnDefinition = "SMALLINT")
    private Integer isRemove = 0;

    @Basic
    @Column(name = "IS_ESIGN",columnDefinition = "SMALLINT")
    private Integer isEsign = 1;

    @Basic
    @Column(name = "PHONE_ESIGN",length = 20)
    private String phoneEsign;



    @Basic
    @Column(name = "LOAN_AMOUNT")
    private BigDecimal loanAmount;

    @Basic
    @Column(name = "TENOR")
    private BigDecimal tenor;

    @Basic
    @Column(name = "MONTHLY_INSTALLMENT_AMOUNT")
    private BigDecimal monthlyInstallmentAmount;

    @Basic
    @Column(name = "MONTHLY_DUE_DATE")
    private BigDecimal monthlyDueDate;

    @Basic
    @Column(name = "CONTRACT_COMPLETE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractCompleteDate;

    @Basic
    @Column(name = "DOCUMENT_VERIFICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date documentVerificationDate;

    @Basic
    @Column(name = "CONTRACT_PRINTING_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractPrintingDate;

    @Basic
    @Column(name = "FIRST_DUE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstDue;

    @Basic
    @Column(name = "END_DUE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDue;

    // string
    @Basic
    @Column(name = "LAST_UPDATE_APPLICANT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateApplicant;

    // string
    @Basic
    @Column(name = "CONTRACT_COMPLETE_DATE_TEMP")
    private String contractCompleteDateTemp;

    @Basic
    @Column(name = "DOCUMENT_VERIFICATION_DATE_TEMP")
    private String documentVerificationDateTemp;

    @Basic
    @Column(name = "CONTRACT_PRINTING_DATE_TEMP")
    private String contractPrintingDateTemp;

    @Basic
    @Column(name = "FIRST_DUE_TEMP")
    private String firstDueTemp;

    @Basic
    @Column(name = "END_DUE_TEMP")
    private String endDueTemp;

    @Basic
    @Column(name = "IS_INSURANCE")
    private char isInsurance;


    @Basic
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Basic
    @Column(name = "MODIFIED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Basic
    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @Basic
    @Column(name = "MODIFIED_BY")
    private UUID modifiedBy;

    @Basic
    @Column(name = "scheme")
    private String scheme;

    @Basic
    @Column(name = "MATURED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date maturedDate;

    @Basic
    @Column(name = "EARLY_TERMINATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date earlyTerminationDate;

    @Basic
    @Column(name = "LAST_SYNCHRONIZED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSynchronizedDate;

    @Basic
    @Column(name = "LAST_SYNC_REPAYMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSyncRepaymentDate;


    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(UUID contractUuid) {
        this.contractUuid = contractUuid;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getLendingCoreContractId() {
        return lendingCoreContractId;
    }

    public void setLendingCoreContractId(String lendingCoreContractId) {
        this.lendingCoreContractId = lendingCoreContractId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }


    public Integer getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(Integer isRemove) {
        this.isRemove = isRemove;
    }

    public Integer getIsEsign() {
        return isEsign;
    }

    public void setIsEsign(Integer isEsign) {
        this.isEsign = isEsign;
    }

    public String getPhoneEsign() {
        return phoneEsign;
    }

    public void setPhoneEsign(String phoneEsign) {
        this.phoneEsign = phoneEsign;
    }

    public Date getContractCompleteDate() {
        return contractCompleteDate;
    }

    public void setContractCompleteDate(Date contractCompleteDate) {
        this.contractCompleteDate = contractCompleteDate;
    }

    public Date getDocumentVerificationDate() {
        return documentVerificationDate;
    }

    public void setDocumentVerificationDate(Date documentVerificationDate) {
        this.documentVerificationDate = documentVerificationDate;
    }

    public Date getContractPrintingDate() {
        return contractPrintingDate;
    }

    public void setContractPrintingDate(Date contractPrintingDate) {
        this.contractPrintingDate = contractPrintingDate;
    }

    public Date getFirstDue() {
        return firstDue;
    }

    public void setFirstDue(Date firstDue) {
        this.firstDue = firstDue;
    }

    public Date getEndDue() {
        return endDue;
    }

    public void setEndDue(Date endDue) {
        this.endDue = endDue;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UUID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public char getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(char isInsurance) {
        this.isInsurance = isInsurance;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getTenor() {
        return tenor;
    }

    public void setTenor(BigDecimal tenor) {
        this.tenor = tenor;
    }

    public BigDecimal getMonthlyInstallmentAmount() {
        return monthlyInstallmentAmount;
    }

    public void setMonthlyInstallmentAmount(BigDecimal monthlyInstallmentAmount) {
        this.monthlyInstallmentAmount = monthlyInstallmentAmount;
    }

    public BigDecimal getMonthlyDueDate() {
        return monthlyDueDate;
    }

    public void setMonthlyDueDate(BigDecimal monthlyDueDate) {
        this.monthlyDueDate = monthlyDueDate;
    }

    public String getContractCompleteDateTemp() {
        return contractCompleteDateTemp;
    }

    public void setContractCompleteDateTemp(String contractCompleteDateTemp) {
        this.contractCompleteDateTemp = contractCompleteDateTemp;
    }

    public String getDocumentVerificationDateTemp() {
        return documentVerificationDateTemp;
    }

    public void setDocumentVerificationDateTemp(String documentVerificationDateTemp) {
        this.documentVerificationDateTemp = documentVerificationDateTemp;
    }

    public String getContractPrintingDateTemp() {
        return contractPrintingDateTemp;
    }

    public void setContractPrintingDateTemp(String contractPrintingDateTemp) {
        this.contractPrintingDateTemp = contractPrintingDateTemp;
    }

    public String getFirstDueTemp() {
        return firstDueTemp;
    }

    public void setFirstDueTemp(String firstDueTemp) {
        this.firstDueTemp = firstDueTemp;
    }

    public String getEndDueTemp() {
        return endDueTemp;
    }

    public void setEndDueTemp(String endDueTemp) {
        this.endDueTemp = endDueTemp;
    }

    public Date getMaturedDate() {
        return maturedDate;
    }

    public void setMaturedDate(Date maturedDate) {
        this.maturedDate = maturedDate;
    }

    public Date getEarlyTerminationDate() {
        return earlyTerminationDate;
    }

    public void setEarlyTerminationDate(Date earlyTerminationDate) {
        this.earlyTerminationDate = earlyTerminationDate;
    }

    public Date getLastSynchronizedDate() {
        return lastSynchronizedDate;
    }

    public void setLastSynchronizedDate(Date lastSynchronizedDate) {
        this.lastSynchronizedDate = lastSynchronizedDate;
    }

    public Date getLastSyncRepaymentDate() {
        return lastSyncRepaymentDate;
    }

    public void setLastSyncRepaymentDate(Date lastSyncRepaymentDate) {
        this.lastSyncRepaymentDate = lastSyncRepaymentDate;
    }

    public Date getLastUpdateApplicant() {
        return lastUpdateApplicant;
    }

    public void setLastUpdateApplicant(Date lastUpdateApplicant) {
        this.lastUpdateApplicant = lastUpdateApplicant;
    }

    public Integer getIsSignUp() {
        return isSignUp;
    }

    public void setIsSignUp(Integer isSignUp) {
        this.isSignUp = isSignUp;
    }

    @Override
    public void validatePayload() {

    }

    public static final class FILE_TYPE {
        public static final String E_SIGN = "11";
        public static final String HISTORY = "12";
        public static final String ADJUSTMENT = "21";
        public static final String BACKGROUND = "BG";
    }
    public static final class SIGN_TYPE {
        public static final int E_SIGN = 1;
        public static final int ADJUSTMENT = 2;
        public static final int OTHER = 3;
    }
}
