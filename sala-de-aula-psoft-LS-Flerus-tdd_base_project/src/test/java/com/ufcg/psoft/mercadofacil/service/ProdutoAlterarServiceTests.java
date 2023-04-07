package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do Servico de alteracao do produto")
public class ProdutoAlterarServiceTests {

    @Autowired
    ProdutoAlterarService driver;

    @MockBean
    ProdutoRepository<Produto, Long> produtoRepository;

    Produto produto;

    @BeforeEach
    void setup() {
        Mockito.when(produtoRepository.find(10L))
                .thenReturn(Produto.builder()
                        .id(10L)
                        .codigoBarra("7899137500100")
                        .nome("Produto Dez")
                        .fabricante("Empresa Dez")
                        .preco(450.00)
                        .build()
                );
        produto = produtoRepository.find(10L);
    }

    @Test
    @DisplayName("Quando um novo nome valido for fornecido para o produto")
    void nomeValido() {
        // Arrange
        produto.setNome("Produto Dez Atualizado");
        Mockito.when(produtoRepository.update(produto))
                .thenReturn(Produto.builder()
                        .id(10L)
                        .codigoBarra("7899137500100")
                        .nome("Produto Dez Atualizado")
                        .fabricante("Empresa Dez")
                        .preco(450.00)
                        .build()
                );

        // Act
        Produto resultado = driver.alterar(produto);

        // Assert
        assertEquals("Produto Dez Atualizado", resultado.getNome());
    }

    @Test
    @DisplayName("Quando o preco menor ou igual a zero")
    void precoMenorIgualAZero() {
        //Arrange
        produto.setPreco(0.0);
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Preco invalido!", thrown.getMessage());
    }


    @Test
    @DisplayName("Quando o nome e invalido")
    void nomeInvalido() {
        //Arrange
        produto.setNome(null);
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Argumentos Incompletos", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o fabricante e invalido")
    void fabricanteInvalido() {
        //Arrange
        produto.setFabricante(null);
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Argumentos Incompletos", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o id e invalido")
    void idInvalido() {
        //Arrange
        produto.setId(null);
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Argumentos Incompletos", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o codigo e nulo")
    void codigoNull() {
        //Arrange
        produto.setCodigoBarra(null);
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Argumentos Incompletos", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o codigo e diferente de 13 numeros")
    void codigoTamanhoErrado() {
        //Arrange
        produto.setCodigoBarra("123");
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Codigo de barras invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o codigo do pais e errado")
    void codigoPaisErrado() {
        //Arrange
        produto.setCodigoBarra("7849137500100");
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Codigo de pais invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o codigo da empresa e errado")
    void codigoEmpresaErrado() {
        //Arrange
        produto.setCodigoBarra("7899137000100");
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Codigo de empresa invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o codigo verificador e errado")
    void codigoVerificadorErrado() {
        //Arrange
        produto.setCodigoBarra("7899137500104");
        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );
        //Assert
        assertEquals("Verificador invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando altero o fabricante")
    void alterarFabricante() {
        /* AAA Pattern */
        //Arrange
        produto.setFabricante("Empresa Onze");
        //Act
        Mockito.when(produtoRepository.update(produto))
        .thenReturn(Produto.builder()
                .id(10L)
                .codigoBarra("7899137500100")
                .nome("Produto Dez Atualizado")
                .fabricante("Empresa Onze")
                .preco(450.00)
                .build()
        );
        Produto resultado = driver.alterar(produto);
        //Assert
        assertEquals("Empresa Onze", resultado.getFabricante());
    }

    @Test
    @DisplayName("Quando altero o preco")
    void alterarPreco() {
        /* AAA Pattern */
        //Arrange
        produto.setPreco(500.00);
        //Act
        Mockito.when(produtoRepository.update(produto))
        .thenReturn(Produto.builder()
                .id(10L)
                .codigoBarra("7899137500100")
                .nome("Produto Dez Atualizado")
                .fabricante("Empresa Onze")
                .preco(500.00)
                .build()
        );
        Produto resultado = driver.alterar(produto);
        //Assert
        assertEquals(500.00, resultado.getPreco());
    }

    @Test
    @DisplayName("Quando altero o codigo")
    void alterarCodigo() {
        /* AAA Pattern */
        //Arrange
        produto.setCodigoBarra("7899137500100");
        //Act
        Mockito.when(produtoRepository.update(produto))
        .thenReturn(Produto.builder()
                .id(10L)
                .codigoBarra("7899137510000")
                .nome("Produto Dez Alterado")
                .fabricante("Empresa Onze")
                .preco(500.00)
                .build()
        );
        Produto resultado = driver.alterar(produto);
        //Assert
        assertEquals("7899137510000", resultado.getCodigoBarra());
    }

}
