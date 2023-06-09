package com.example.progettoprova.services.impl;

import com.example.progettoprova.config.MessagesConfig;
import com.example.progettoprova.dao.ProdottoDao;

import com.example.progettoprova.dto.ImageDto;
import com.example.progettoprova.dto.ProdottoDto;
import com.example.progettoprova.entities.Image;
import com.example.progettoprova.entities.Prodotto;
import com.example.progettoprova.exception.ProdottoException;
import com.example.progettoprova.exception.UtenteException;
import com.example.progettoprova.services.ImmagineService;
import com.example.progettoprova.services.ProdottoService;
import com.example.progettoprova.services.UtenteService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class ProdottoServiceImpl implements ProdottoService {

    private final ProdottoDao prodottoDao;
    private final ModelMapper modelMapper;
    private final UtenteService utenteService;
    private final ImmagineService immagineService;

    @Override
    @SneakyThrows
    @Transactional
    public List<ProdottoDto> dammiProdotti() {

        List<Prodotto> prodotti = prodottoDao.findAll();
        if(prodotti.isEmpty())
            throw new ProdottoException(MessagesConfig.PRODOTTI_NON_TROVATI);
        return prodotti.stream().map(prodotto -> modelMapper.map(prodotto, ProdottoDto.class)).collect(Collectors.toList());
    }


    @Override
    @SneakyThrows
    @Transactional
    public List<ProdottoDto> dammiProdottiDiUnUtenteById(Long id) {
         List<Prodotto> prodotti=prodottoDao.findAllByVenditoreId(id);
         if(prodotti.isEmpty())
             throw new ProdottoException(MessagesConfig.PRODOTTI_NON_TROVATI);
        return prodotti.stream().map(prodotto -> modelMapper.map(prodotto, ProdottoDto.class)).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    @Transactional
    public ProdottoDto dammiProdottoById(Long id) {
        Prodotto prodotto = prodottoDao.findById(id).orElseThrow(() -> new ProdottoException(MessagesConfig.PRODOTTO_NON_TROVATO_ID + id));
        return  modelMapper.map(prodotto, ProdottoDto.class);
    }


    @Override
    @SneakyThrows
    public void salva(ProdottoDto p) {
        log.info("arrivo con prodotto : "+p.getNomeProdotto());
        log.info("arrivo con prodotto : "+p.getVenditoreId());
        if(utenteService.dammiUtente(p.getVenditoreId())==null)
            throw new UtenteException(MessagesConfig.UTENTE_NON_TROVATO_ID+p.getVenditoreId());

        log.info("sono dentro salva service");
        List<ImageDto> imagesDto=p.getImages();
        p.setImages(new ArrayList<>());
        Prodotto prodotto = prodottoDao.save(modelMapper.map(p, Prodotto.class));

        System.out.println("prodotto ritornato"+prodotto);
        for (ImageDto imageDto:imagesDto){
            Image image=modelMapper.map(imageDto, Image.class);
            image.setProdotto(prodotto);
            immagineService.salva(image);

        }


        log.info(MessagesConfig.PRODOTTO_SALVATO_NOME_LOG+p.getNomeProdotto());
    }

    @Override
    public void salva2(Prodotto p) {
        prodottoDao.save(p);
    }

    @Override
    public List<ProdottoDto> dammiProdottiDiUnUtenteByIdOrdCrescByPrezzo(Long id) {

        return prodottoDao.findAllByVenditoreIdOrderByPrezzoAsc(id)
                        .stream().map(prodotto -> modelMapper.map(prodotto,ProdottoDto.class))
                        .collect(Collectors.toList());
    }


    @Override
    @SneakyThrows
    public void cancella(Long id) {
        Optional<Prodotto> prodotto = prodottoDao.findById(id);
        if(prodotto.isEmpty())
            throw new ProdottoException(MessagesConfig.PRODOTTO_NON_TROVATO_ID +id);
        prodottoDao.delete(prodotto.get());
        log.info(MessagesConfig.PRODOTTO_CANCELLATO_NOME_LOG+prodotto.get().getNomeProdotto());
    }

    @Override
    @SneakyThrows
    public ProdottoDto aggiorna(Long id, ProdottoDto prodotto) {
        Optional<Prodotto> p = prodottoDao.findById(id);
        if(p.isEmpty())
            throw new ProdottoException(MessagesConfig.PRODOTTO_NON_TROVATO_ID +id);
//        p.get().setPrezzo(prodotto.getPrezzo());
        prodottoDao.save(p.get());
        log.info(MessagesConfig.PRODOTTO_AGGIORNATO_ID_LOG, id);
        return modelMapper.map(prodottoDao.save(p.get()),ProdottoDto.class);
    }


//    cancellare?
    @Override
    @Transactional
    public List<Image> dammiImmaginiByIdProdotto(Long id) {
        return prodottoDao.dammiListaImmaginiByProdottoId(id);

    }


    @Override
    @SneakyThrows
    public ProdottoDto salvaWeb(ProdottoDto p) {
        log.info("arrivo con prodotto : "+p.getNomeProdotto());
        log.info("arrivo con prodotto : "+p.getVenditoreId());
        if(utenteService.dammiUtente(p.getVenditoreId())==null)
            throw new UtenteException(MessagesConfig.UTENTE_NON_TROVATO_ID+p.getVenditoreId());

        log.info("sono dentro salva service");
        List<ImageDto> imagesDto=p.getImages();
        p.setImages(new ArrayList<>());
        Prodotto prodotto = prodottoDao.save(modelMapper.map(p, Prodotto.class));

        System.out.println("prodotto ritornato"+prodotto);
        for (ImageDto imageDto:imagesDto){
            Image image=modelMapper.map(imageDto, Image.class);
            image.setProdotto(prodotto);
            immagineService.salva(image);

        }

        ProdottoDto prodottoDto = modelMapper.map(prodotto, ProdottoDto.class);


        log.info(MessagesConfig.PRODOTTO_SALVATO_NOME_LOG+p.getNomeProdotto());

        return prodottoDto;
    }

}
