package com.mycompany.moneytransfers.api;

import static com.mycompany.moneytransfers.api.ApiTest.jerseyClient;
import com.mycompany.moneytransfers.dto.ApiResponse;
import com.mycompany.moneytransfers.dto.Transfer;
import java.util.Map;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Test;

public class TransferApiTest extends ApiTest {

    @Test
    public void sendTest() {
        Transfer transfer = new Transfer();
        transfer.setSenderId(2);
        transfer.setRecipientId(1);
        transfer.setAmount(100);

        Response response = jerseyClient.target(TARGET).path("/transfer/").request(MediaType.APPLICATION_JSON).
                header("Content-type", MediaType.APPLICATION_JSON).post(Entity.entity(transfer, MediaType.APPLICATION_JSON));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ApiResponse responseEntity = response.readEntity(ApiResponse.class);
        Assert.assertEquals(0, responseEntity.getCode());
        assertAccount(1, Response.Status.OK, 1300);
        assertAccount(2, Response.Status.OK, 2000);

        transfer.setRecipientId(2);
        transfer.setSenderId(1);
        
        response = jerseyClient.target(TARGET).path("/transfer/").request(MediaType.APPLICATION_JSON).
                header("Content-type", MediaType.APPLICATION_JSON).post(Entity.entity(transfer, MediaType.APPLICATION_JSON));
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        responseEntity = response.readEntity(ApiResponse.class);
        Assert.assertEquals(0, responseEntity.getCode());
        assertAccount(1, Response.Status.OK, 1200);
        assertAccount(2, Response.Status.OK, 2100);

    }

    @Test
    public void sendTooMuchTest() {
        Transfer transfer = new Transfer();
        transfer.setSenderId(2);
        transfer.setRecipientId(1);
        transfer.setAmount(100000);

        Response response = jerseyClient.target(TARGET).path("/transfer/").request(MediaType.APPLICATION_JSON).
                header("Content-type", MediaType.APPLICATION_JSON).post(Entity.entity(transfer, MediaType.APPLICATION_JSON));
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ApiResponse responseEntity = response.readEntity(ApiResponse.class);
        Assert.assertEquals(1, responseEntity.getCode());
        Assert.assertEquals("Sender balance is too low", responseEntity.getMessage());
        assertAccount(1, Response.Status.OK, 1200);
        assertAccount(2, Response.Status.OK, 2100);

    }

    @Test
    public void sendToUnexistingAccountTest() {
        Transfer transfer = new Transfer();
        transfer.setSenderId(2);
        transfer.setRecipientId(1356);
        transfer.setAmount(10);

        Response response = jerseyClient.target(TARGET).path("/transfer/").request(MediaType.APPLICATION_JSON).
                header("Content-type", MediaType.APPLICATION_JSON).post(Entity.entity(transfer, MediaType.APPLICATION_JSON));
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ApiResponse responseEntity = response.readEntity(ApiResponse.class);
        Assert.assertEquals(1, responseEntity.getCode());
        Assert.assertEquals("Recipient account was not found", responseEntity.getMessage());
        assertAccount(1, Response.Status.OK, 1200);
        assertAccount(2, Response.Status.OK, 2100);

    }

    @Test
    public void sendFromUnexistingAccountTest() {
        Transfer transfer = new Transfer();
        transfer.setSenderId(8743256);
        transfer.setRecipientId(1);
        transfer.setAmount(10);

        Response response = jerseyClient.target(TARGET).path("/transfer/").request(MediaType.APPLICATION_JSON).
                header("Content-type", MediaType.APPLICATION_JSON).post(Entity.entity(transfer, MediaType.APPLICATION_JSON));
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ApiResponse responseEntity = response.readEntity(ApiResponse.class);
        Assert.assertEquals(1, responseEntity.getCode());
        Assert.assertEquals("Sender account was not found", responseEntity.getMessage());
        assertAccount(1, Response.Status.OK, 1200);
        assertAccount(2, Response.Status.OK, 2100);

    }

    private void assertAccount(long id, Response.Status status, double balance) {

        Response response = jerseyClient.target(TARGET).path("/accounts/" + id).request(MediaType.APPLICATION_JSON).
                header("Content-type", MediaType.APPLICATION_JSON).get();
        Assert.assertEquals(status.getStatusCode(), response.getStatus());
        ApiResponse responseEntity = response.readEntity(ApiResponse.class);
        Assert.assertEquals(0, responseEntity.getCode());
        Map<String, Object> accountDetails = (Map<String, Object>) responseEntity.getEntity();
        Assert.assertEquals(id, ((Integer) accountDetails.get("id")).longValue());
        Assert.assertEquals(balance, (Double) accountDetails.get("balance"), 0);
    }

}
