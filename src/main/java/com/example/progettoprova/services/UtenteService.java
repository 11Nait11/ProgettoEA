package com.example.progettoprova.services;


import com.example.progettoprova.dto.ProdottoDto;
import com.example.progettoprova.dto.RecensioneDto;
import com.example.progettoprova.dto.UtenteDto;
import com.example.progettoprova.entities.Utente;

import java.util.List;
import java.util.Map;

public interface UtenteService {


    public List<UtenteDto> dammiUtenti();
    public UtenteDto dammiUtente(Long id);
    public List<ProdottoDto> dammiProdottiUtente(Long id);
    public Utente dammiEntityUtente(Long id);
    public UtenteDto dammiUtenteByUsername(String username);

    public List<RecensioneDto> dammiRecensioniUtente(Long id);

    public void salva(Utente u);
    public void salvaDto(UtenteDto uD);
    public void cancella(Long id);
    public UtenteDto aggiorna(Long id, UtenteDto utente);

    Map<String, String> refreshToken(String authorizationHeader, String string);
}
