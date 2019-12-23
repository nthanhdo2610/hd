package com.tinhvan.hd.dto;

import com.tinhvan.hd.base.BadRequestException;
import com.tinhvan.hd.base.HDPayload;
import com.tinhvan.hd.base.HDUtil;
import com.tinhvan.hd.entity.ContractFilePosition;

import java.util.List;

public class ContractFilePositionRequest implements HDPayload {
    private String filePath;
    private List<ContractFilePosition> positions;

    @Override
    public void validatePayload() {
        if(HDUtil.isNullOrEmpty(filePath)){
            throw new BadRequestException(1426);
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<ContractFilePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<ContractFilePosition> positions) {
        this.positions = positions;
    }
}
