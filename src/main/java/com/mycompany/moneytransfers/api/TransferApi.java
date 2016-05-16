package com.mycompany.moneytransfers.api;

import com.mycompany.moneytransfers.dto.ApiResponse;
import com.mycompany.moneytransfers.dto.Transfer;
import com.mycompany.moneytransfers.exceptions.InternalServerException;
import com.mycompany.moneytransfers.exceptions.RequestException;
import com.mycompany.moneytransfers.services.TransferService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
public class TransferApi {

    private TransferService transferService = TransferService.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response send(Transfer transfer) {
        Response.ResponseBuilder response = null;
        try {
            transferService.sendTransfer(transfer);
            response = Response.ok(new ApiResponse(0, null));
        } catch (InternalServerException e) {
            response = Response.serverError().entity(new ApiResponse(1, e.getMessage()));
        } catch (RequestException e) {
            response = Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(1, e.getMessage()));
        }
        return response.build();
    }

}
