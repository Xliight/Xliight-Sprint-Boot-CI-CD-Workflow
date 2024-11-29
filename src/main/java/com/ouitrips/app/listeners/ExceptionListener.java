package com.ouitrips.app.listeners;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
@Service
public class ExceptionListener {
    @Transactional
    public void handleException(Throwable event){
        event.printStackTrace();
    }
}