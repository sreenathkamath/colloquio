package com.colloquio.resources;

import com.codahale.metrics.annotation.Timed;
import com.colloquio.api.Info;
import com.colloquio.core.Candidate;
import com.colloquio.db.CandidatesDao;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/candidate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CandidatesResouce {

    private final CandidatesDao candidatesDao;

    public CandidatesResouce(CandidatesDao candidatesDao) {
        this.candidatesDao = candidatesDao;
    }

    @POST
    @Timed
    @PermitAll
    public Info createCandidate(
            @Valid @NotNull Candidate candidate){
        long candidateCount = candidatesDao.doesCandidateExist(candidate);
        if (candidateCount > 0) {
            throw new NotAcceptableException("Candidate with this email_id or phone_number already exits");
        }
        long candidateId = candidatesDao.createCandidate(candidate);
        return new Info(candidateId, candidate.getFullName());
    }

    @GET
    @Timed
    @PermitAll
    public List<Candidate> getAllCandidates(){
        return candidatesDao.getAllCandidates();
    }

}
