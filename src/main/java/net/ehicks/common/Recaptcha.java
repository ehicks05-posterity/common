package net.ehicks.common;

import net.tanesha.recaptcha.ReCaptchaImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Recaptcha
{
    public static boolean isPassesCaptcha(String remoteAddr, String privateKey, String gRecaptchaResponse) throws IOException
    {
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(privateKey);

        String USER_AGENT = "Mozilla/5.0";

        String url = "https://www.google.com/recaptcha/api/siteverify?";
        url += "secret=" + privateKey;
        url += "&response=" + gRecaptchaResponse;
        url += "&remoteip=" + remoteAddr;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {response.append(inputLine);}
        in.close();

        //print result
        return response.toString().contains("true");
    }
}
