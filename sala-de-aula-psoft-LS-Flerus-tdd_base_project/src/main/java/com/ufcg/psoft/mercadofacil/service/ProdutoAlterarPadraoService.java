package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoAlterarPadraoService implements ProdutoAlterarService {
    @Autowired
    ProdutoRepository<Produto, Long> produtoRepository;
    @Override
    public Produto alterar(Produto produto) {

        if(produto.getNome() == null || produto.getFabricante() == null ||
                produto.getCodigoBarra() == null || produto.getId() == null) {
            throw new RuntimeException("Argumentos Incompletos");
        }

        if(produto.getPreco() <= 0) {
            throw new RuntimeException("Preco invalido!");
        }

        if(produto.getCodigoBarra().length() != 13) {
            throw new RuntimeException("Codigo de barras invalido!");
        }

        if(!String.valueOf(produto.getCodigoBarra().charAt(0)).equals("7") ||
                !String.valueOf(produto.getCodigoBarra().charAt(1)).equals("8") ||
                !String.valueOf(produto.getCodigoBarra().charAt(2)).equals("9")) {
            throw new RuntimeException("Codigo de pais invalido!");
        }

        if(!String.valueOf(produto.getCodigoBarra().charAt(3)).equals("9") ||
                !String.valueOf(produto.getCodigoBarra().charAt(4)).equals("1") ||
                !String.valueOf(produto.getCodigoBarra().charAt(5)).equals("3") ||
                !String.valueOf(produto.getCodigoBarra().charAt(6)).equals("7") ||
                !String.valueOf(produto.getCodigoBarra().charAt(7)).equals("5")) {
            throw new RuntimeException("Codigo de empresa invalido!");
        }

        int somaImpar = 0;
        for(int i = 0; i < 11; i+= 2) {
            somaImpar += Integer.parseInt(String.valueOf(produto.getCodigoBarra().charAt(i)));
        }
        int somaPar = 0;
        for(int i = 1; i < 12; i += 2) {
            somaPar += Integer.parseInt(String.valueOf(produto.getCodigoBarra().charAt(i)));
        }
        somaPar *= 3;
        int verificador = 10 - ((somaPar + somaImpar)%10);
        if(verificador == 10) {
            verificador = 0;
        }

        if(verificador != Integer.parseInt(String.valueOf(produto.getCodigoBarra().charAt(12)))) {
            throw new RuntimeException("Verificador invalido!");
        }

        return produtoRepository.update(produto);
    }
}