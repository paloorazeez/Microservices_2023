package com.azeez.learning.controller;

import com.azeez.learning.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RefreshScope
public class ConfigResource {

    @Autowired
    ConfigService configService;

    @GetMapping("/property/{propName}")
    public Object getMessage(@PathVariable("propName") String propertyName){
        return configService.getPropertyAsString(propertyName);
    }

    @PostMapping(path="/reset")
    public ResponseEntity doRefresh(@RequestBody String body){

        int responseCode=0;
        try {

            final String POST_PARAMS = "";
            System.out.println(POST_PARAMS);
            URL obj = new URL("http://localhost:8888/actuator/bus-refresh");
            HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setDoOutput(true);
            OutputStream os = postConnection.getOutputStream();
            os.write(POST_PARAMS.getBytes());
            os.flush();
            os.close();
            responseCode = postConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        postConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
// print result
                System.out.println(response.toString());
                System.out.println(" — — — -Configuration Refreshed Successfully — — — -");
            } else {
                System.out.println("POST NOT WORKED");
            }
        } catch (Exception ex){
            ex.getStackTrace();
        }

        return new ResponseEntity<>("Configuration Refreshed Sucessfully", HttpStatus.OK);
    }
}
