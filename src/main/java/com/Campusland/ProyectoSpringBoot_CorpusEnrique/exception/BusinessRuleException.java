package com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception;

public class BusinessRuleException extends RuntimeException{
    public BusinessRuleException(String mensaje){
        super(mensaje);
    }
}
