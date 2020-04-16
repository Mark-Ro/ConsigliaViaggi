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
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryStrutturaGPS(RequestDetailsStrutturaGPS requestDetailsStrutturaCitta);
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryCitta(RequestDetailsTable requestDetailsTable);
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryGallery(RequestDetailsTable requestDetailsTable);
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryRecensioni(RequestDetailsTable requestDetailsTable);
    @LambdaFunction
    ResponseDetailsUpdate funzioneLambdaInsertRecensione(RequestDetailsRecensione requestDetailsRecensione);
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryMieRecensioni(RequestDetailsTable requestDetailsTable);
    @LambdaFunction
    ResponseDetailsUpdate funzioneLambdaUpdateRecensione(RequestDetailsUpdateRecensione requestDetailsUpdateRecensione);
    @LambdaFunction
    ResponseDetailsUpdate funzioneLambdaDeleteRecensione(RequestDetailsTable requestDetailsTable);
    @LambdaFunction
    ResponseDetailsQuery funzioneLambdaQueryStruttura(RequestDetailsTable requestDetailsTable);

}
