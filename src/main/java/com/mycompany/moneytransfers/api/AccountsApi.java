package com.mycompany.moneytransfers.api;

import com.mycompany.moneytransfers.dto.AccountDetails;
import com.mycompany.moneytransfers.dto.ApiResponse;
import com.mycompany.moneytransfers.exceptions.InternalServerException;
import com.mycompany.moneytransfers.services.AccountsService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts/{id}")
public class AccountsApi {

    private AccountsService accountsService = AccountsService.getInstance();

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountDetails(@PathParam("id") long id) {
        Response.ResponseBuilder response = null;
        try {
            AccountDetails accountDetails = accountsService.getAccountDetails(id);
            if (accountDetails == null) {
                response = Response.status(Response.Status.BAD_REQUEST).entity(new ApiResponse(1, "Account was not found"));
            } else {
                response = Response.ok(new ApiResponse(0, accountsService.getAccountDetails(id)));
            }
        } catch (InternalServerException e) {
            response = Response.serverError().entity(new ApiResponse(1, e.getMessage()));
        }
        return response.build();
    }

}
