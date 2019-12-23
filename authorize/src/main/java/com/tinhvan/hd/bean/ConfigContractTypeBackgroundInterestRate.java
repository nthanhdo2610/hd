package com.tinhvan.hd.bean;

public class ConfigContractTypeBackgroundInterestRate {
    private String contractName;
    private double interestRate;

    public ConfigContractTypeBackgroundInterestRate() {
    }

    public ConfigContractTypeBackgroundInterestRate(String contractName, double interestRate) {
        this.contractName = contractName;
        this.interestRate = interestRate;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public String toString() {
        return "ConfigContractTypeBackgroundInterestRate{" +
                "contractName='" + contractName + '\'' +
                ", interestRate=" + interestRate +
                '}';
    }
}
