package com.example.rmfr.result;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class RestResults {

    private int resultCode;
    private String resultMessage;
    private Map<String, Object> result;

    // resultCode : 20X - OK
    private static int RSLT_200 = 200;
    private static String RSLT_200_MSG = "정상적으로 완료되었습니다.";

    // resultCode : 40X - NOT_FOUND
    private static int RSLT_400 = 400;
    private static String RSLT_400_MSG = "를 찾지 못했습니다.";
    private static int RSLT_401 = 401;
    private static String RSLT_401_MSG = "회원만 사용할 수 있는 기능입니다.";

    // resultCode : 50X - SYSTEM_ERROR
    private static int RSLT_500 = 500;
    private static String RSLT_500_MSG = "시스템 오류가 발생하였습니다.";

    public RestResults() {
        this.resultCode = 0;
        this.resultMessage = "";
        this.result  = new HashMap<>();
    }

    public void setResultCode(int code) {
        this.resultCode = code;

        if ( code == 200 ) {
            this.resultMessage = RSLT_200_MSG;
        } else if ( code == 400 ) {
            this.resultMessage = RSLT_400_MSG;
        } else if ( code == 401 ) {
            this.resultMessage = RSLT_401_MSG;
        } else if ( code == 500 ) {
            this.resultMessage = RSLT_500_MSG;
        } else {
            this.resultMessage = RSLT_500_MSG;
        }
    }

    public void setResult(String key, Object val) {
        this.result.put(key, val);
    }

    public void setResultMessage(String msg, int type) {
        if ( type == 0 ) {
            this.resultMessage = msg + this.resultMessage;
        } else if ( type == 1 ) {
            this.resultMessage = msg;
        }
    }
}
