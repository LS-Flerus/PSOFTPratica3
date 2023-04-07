package com.ufcg.psoft.mercadofacil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Produtos")
public class ProdutoV1ControllerTests {
    @Autowired
    MockMvc driver;

    @Autowired
    ProdutoRepository<Produto, Long> produtoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Produto produto;

    @BeforeEach
    void setup() {
        produto = produtoRepository.find(10L);
    }

    @AfterEach
    void tearDown() {
        produto = null;
    }

    @Nested
    @DisplayName("Conjunto de casos de verificacao de campos obrigatorios")
    class ProdutoValidacaoCamposObrigatorios {

        @Test
        @DisplayName("Quando altera o fabricante")
        void alterarFabricante() throws Exception {
            /* AAA Pattern */
            //Arrange
            produto.setFabricante("Empresa Onze");
            //Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();
            //Assert
            assertEquals("Empresa Onze", resultado.getFabricante());
        }

        @Test
        @DisplayName("Quando alteramos o nome do produto com dados validos")
        void quandoAlteramosNomeDoProdutoValido() throws Exception {
            // Arrange
            produto.setNome("Produto Dez Alterado");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getNome(), "Produto Dez Alterado");
        }

        @Test
        @DisplayName("Quando o nome e invalido")
        void nomeInvalido() {
            //Arrange
            produto.setNome(null);
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Argumentos Incompletos", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o fabricante e invalido")
        void fabricanteInvalido() {
            //Arrange
            produto.setFabricante(null);
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Argumentos Incompletos", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o codigo e nulo")
        void codigoNull() {
            //Arrange
            produto.setCodigoBarra(null);
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Argumentos Incompletos", thrown.getMessage());
        }

    }

    @Nested
    @DisplayName("Conjunto de casos de verificacao da regra sobre o preco")
    class ProdutoValidacaoRegrasDoPreco {
        @Test
        @DisplayName("Quando o preco menor ou igual a zero")
        void precoMenorIgualAZero() {
            //Arrange
            produto.setPreco(0.0);
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Preco invalido!", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o preco e valido")
        void alterarPreco() throws Exception {
            /* AAA Pattern */
            //Arrange
            produto.setPreco(500.00);
            //Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();
            //Assert
            assertEquals(500.00, resultado.getPreco());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificacao da validade do codigo de barras")
    class ProdutoValidacaoCodigoDeBarras {

        @Test
        @DisplayName("Quando o codigo e correto")
        void alterarCodigo() throws Exception {
            /* AAA Pattern */
            //Arrange
            produto.setCodigoBarra("7899137510000");
            //Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();
            //Assert
            assertEquals("7899137510000", resultado.getCodigoBarra());
        }

        @Test
        @DisplayName("Quando o codigo e diferente de 13 numeros")
        void codigoTamanhoErrado() throws Exception {
            //Arrange
            produto.setCodigoBarra("123");
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Codigo de barras invalido!", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o codigo do pais e errado")
        void codigoPaisErrado() throws Exception {
            //Arrange
            produto.setCodigoBarra("7849137500100");
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Codigo de pais invalido!", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o codigo de empresa errado")
        void codigoEmpresaErrado() throws Exception {
            //Arrange
            produto.setCodigoBarra("7898137500100");
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Codigo de empresa invalido!", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o codigo verificador e errado")
        void codigoVerificadorErrado() {
            //Arrange
            produto.setCodigoBarra("7899137500104");
            //Act
            ServletException thrown = assertThrows(
                    ServletException.class,
                    () -> driver.perform(put("/v1/produtos/" + produto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(produto)))
                            .andExpect(status().isOk())
                            .andDo(print())
                            .andReturn().getResponse().getContentAsString());
            //Assert
            assertEquals("Request processing failed: java.lang.RuntimeException: Verificador invalido!", thrown.getMessage());
        }

    }

}
