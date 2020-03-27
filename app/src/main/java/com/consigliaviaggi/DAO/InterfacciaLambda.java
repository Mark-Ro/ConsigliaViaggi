package com.consigliaviaggi.DAO;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface InterfacciaLambda {
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryUtente(RequestDetailsUtenteQuery requestDetails);
    @LambdaFunction
    ResponseDetailsUpdate funzioneLambdaInserimentoUtente(RequestDetailsUtenteInsert requestDetailsUtenteInsert);
    @LambdaFunction
    ResponseDetailsUpdate funzioneLambdaUpdateUtente(RequestDetailsUpdateUtente requestDetailsUpdateUtente);
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryStrutturaCitta(RequestDetailsStrutturaCitta requestDetailsStrutturaCitta);
}
