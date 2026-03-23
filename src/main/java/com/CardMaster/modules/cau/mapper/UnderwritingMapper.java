package com.CardMaster.modules.cau.mapper;

import com.CardMaster.modules.cau.dto.CreditScoreGenerateRequest;
import com.CardMaster.modules.cau.dto.CreditScoreResponse;
import com.CardMaster.modules.cau.dto.UnderwritingDecisionResponse;
import com.CardMaster.modules.cau.entity.CreditScore;
import com.CardMaster.modules.cau.entity.UnderwritingDecision;
import com.CardMaster.modules.paa.entity.CardApplication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UnderwritingMapper {


    public CreditScore fromCreditScoreRequest(CreditScoreGenerateRequest req, CardApplication app) {
        CreditScore cs = new CreditScore();
        cs.setApplication(app);
        cs.setBureauScore(req.getBureauScore());
        cs.setInternalScore(req.getBureauScore() / 10);
        cs.setGeneratedDate(LocalDateTime.now());
        return cs;
    }

    public CreditScoreResponse toCreditScoreResponse(CreditScore e) {
        CreditScoreResponse d = new CreditScoreResponse();
        d.setScoreId(e.getScoreId());
        d.setApplicationId(e.getApplication().getApplicationId());
        d.setBureauScore(e.getBureauScore());
        d.setInternalScore(e.getInternalScore());
        d.setGeneratedDate(e.getGeneratedDate().toString());
        return d;
    }

    public UnderwritingDecisionResponse toUnderwritingDecisionResponse(UnderwritingDecision e) {
        UnderwritingDecisionResponse d = new UnderwritingDecisionResponse();
        d.setDecisionId(e.getDecisionId());
        d.setApplicationId(e.getApplication().getApplicationId());
        d.setUnderwriterId(e.getUnderwriter().getUserId());
        d.setDecision(e.getDecision());
        d.setApprovedLimit(e.getApprovedLimit());
        d.setRemarks(e.getRemarks());
        d.setDecisionDate(e.getDecisionDate().toString());
        return d;
    }
}
