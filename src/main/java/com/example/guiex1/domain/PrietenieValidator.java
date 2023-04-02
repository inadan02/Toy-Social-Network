package com.example.guiex1.domain;

public class PrietenieValidator implements Validator<Prietenie>{

    /**
     *validate a friendship
     * @param entity - the entity to be validated
     * @throws ValidationException if the friendship is invalid
     */
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        String errors="";
        if(entity == null) throw new ValidationException("Prietenie nula!");
        if(entity.getId().getLeft()==null||entity.getId().getRight()==null)
            errors+="invalid ids for friendships!";
        if(entity.getId().getLeft()==entity.getId().getRight())
            errors+="ids ar equal!";
        if(!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
