package com.colloquio.resources;

import com.codahale.metrics.annotation.Timed;
import com.colloquio.api.Info;
import com.colloquio.core.Skills;
import com.colloquio.db.SkillsDao;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("metadata/skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkillsResource {

    private SkillsDao skillsDao;

    public SkillsResource(SkillsDao skillsDao){
        this.skillsDao = skillsDao;
    }

    @GET
    @Timed
    @PermitAll
    public List<Skills> getSkills(){
        return skillsDao.getSkills();
    }


    @GET
    @Timed
    @PermitAll
    @Path("{id}")
    public Skills getSkills(
            @PathParam("id") @NotNull Long skillId
    ) {
        Optional<Skills> optionalSkills = skillsDao.findSkillById(skillId);
        Skills skills = optionalSkills.orElse(null);
        if (skills == null) {
            throw new NotFoundException(String.format("Cannot find the skill with id: %d", skillId));
        }
        return skills;
    }

    @POST
    @Timed
    @PermitAll
    public Info createSkill(
            @Valid @NotNull Skills skills
    ){
        long count = skillsDao.doesSkillExist(skills.getName());
        if (count > 0) {
            throw new NotAcceptableException(String.format("Skills with name %s already exits", skills.getName()));
        }
        long createdSkillId = skillsDao.createSkill(skills);
        return new Info(createdSkillId, skills.getName());
    }
}
