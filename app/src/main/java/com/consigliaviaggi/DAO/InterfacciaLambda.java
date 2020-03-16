package com.consigliaviaggi.DAO;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface InterfacciaLambda {
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryUtente(RequestDetailsUtenteQuery requestDetails);
    @LambdaFunction
    ResponseDetailsUpdate funzioneLambdaInserimentoUtente(RequestDetailsUtenteInsert requestDetailsUtenteInsert);
}
