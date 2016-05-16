package com.mycompany.moneytransfers.api;

import com.mycompany.moneytransfers.dto.ApiResponse;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Test;

public class AccountsApiTest extends ApiTest {

    @Test
    public void getUnexistingTest() {
        Response response = jerseyClient.target(TARGET).path("/accounts/4").request(MediaType.APPLICATION_JSON).
                header("Content-type", MediaType.APPLICATION_JSON).get();
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ApiResponse responseEntity = response.readEntity(ApiResponse.class);
        Assert.assertEquals(1, responseEntity.getCode());
        Assert.assertEquals("Account was not found", responseEntity.getMessage());

    }

    @Test
    public void getTest() {
        Response response = jerseyClient.target(TARGET).path("/accounts/2").request(MediaType.APPLICATION_JSON).
                header("Content-type", MediaType.APPLICATION_JSON).get();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ApiResponse responseEntity = response.readEntity(ApiResponse.class);
        Assert.assertEquals(0, responseEntity.getCode());
        Map<String, Object> accountDetails = (Map<String, Object>) responseEntity.getEntity();
        Assert.assertEquals(2, accountDetails.get("id"));
        Assert.assertEquals(2100, (Double) accountDetails.get("balance"), 0);

    }

}
