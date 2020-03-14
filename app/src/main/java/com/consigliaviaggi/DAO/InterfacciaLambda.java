package com.consigliaviaggi.DAO;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface InterfacciaLambda {
    @LambdaFunction
    ResponseDetails funzioneLambdaQueryUtente(RequestDetailsUtente requestDetails);
}
