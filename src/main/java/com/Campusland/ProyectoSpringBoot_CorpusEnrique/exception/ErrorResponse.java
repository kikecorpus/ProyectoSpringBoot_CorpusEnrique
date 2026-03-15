package com.Campusland.ProyectoSpringBoot_CorpusEnrique.exception;

import java.time.LocalDateTime;
/**
 * {
 *     "timestamp": 2012...,
 *     "status":....
 *     "message":....
 *     "errorCode":....
 * }
 * */
public record ErrorResponse
        (LocalDateTime timestamp, int status, String message, String errorCode){
}
