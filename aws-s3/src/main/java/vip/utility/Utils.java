package vip.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Utils {

    private Utils(){

    }

    public static final String SECRET_KEY="AVdvd@1999#d";
    public static final int OTP_LENGTH=4;

    public static final String OTP_CHARACTERS = "0123456789";
    public static final String API_KEY = "HbWqSztKO0Rps9Mmjwda34eniLPBgX7uZrkhDlVfGyUI2cT1NQXCSNkIpRFcWoMHlQ67yumG82TBzJt4";


}
