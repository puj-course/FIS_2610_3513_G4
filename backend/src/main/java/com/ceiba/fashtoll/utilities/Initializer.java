package com.ceiba.fashtoll.utilities;

import com.ceiba.fashtoll.exceptionHandling.exceptionTypes.ResourceNotFoundException;
import com.ceiba.fashtoll.security.auth.AuthService;
import com.ceiba.fashtoll.security.auth.dtos.RegisterRequest;
import com.ceiba.fashtoll.utilities.enums.Role;
import com.ceiba.fashtoll.worldModel.brand.BrandService;
import com.ceiba.fashtoll.worldModel.brand.dtos.BrandDTO;
import com.ceiba.fashtoll.worldModel.client.ClientService;
import com.ceiba.fashtoll.worldModel.client.dtos.ClientDTO;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductCreateRequest;
import com.ceiba.fashtoll.worldModel.product.dtos.ProductTypeRequest;
import com.ceiba.fashtoll.worldModel.product.services.ProductService;
import com.ceiba.fashtoll.worldModel.product.services.ProductTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.io.IOException;

@Component
public class Initializer implements CommandLineRunner{
    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);
    @Autowired
    private ResourceLoader resourceLoader;
    private Resource resource;
    private final BrandService brandService;
    private final ClientService clientService;
    private final AuthService authService;
    private final ProductTypeService productTypeService;
    private final ProductService  productService;

    public Initializer(BrandService brandService, ClientService clientService, AuthService authService, ProductTypeService productTypeService, ProductService productService) {
        this.brandService = brandService;
        this.clientService = clientService;
        this.authService = authService;
        this.productTypeService = productTypeService;
        this.productService = productService;
    }

    public void setResource(String file) {
        this.resource = resourceLoader.getResource("classpath:initial data/" + file);
    }

    @Override
    public void run(String... args){
        if (args.length == 0) {
            try{
                throw new RuntimeException("No se recibieron argumentos");
            } catch (RuntimeException e){
                System.out.println();
                logger.error("Error en el arranque de Fashtoll por consola: " + e.getMessage());
                System.out.println();
            }
        } else {
            switch(args[0]){
                case "upload-products":
                    logger.info("Se va a inyectar productos desde el archivo: " + args[1]);
                    this.rearProductJson(args[1], args[2]);

                    break;
                case "upload-product-types":
                    logger.info("Se va a inyectar tipos de producto desde el archivo: " + args[1]);
                    this.readProductTypesJson(args[1]);

                    break;
                case "admins-register":
                    logger.info("Se va a inyectar los dos ADMINS");
                    this.adminsRegister();

                    break;
                case "upload-clients":
                    logger.info("Se va a inyectar clientes desde el archivo: " + args[1]);
                    this.readClientsJson(args[1]);

                    break;
                case "upload-brands":
                    logger.info("Se va a inyectar marcas desde el archivo: " + args[1]);
                    this.readBrandsJson(args[1]);

                    break;
                default:
                    try{
                        throw new RuntimeException("Argumento invalido");
                    } catch (RuntimeException e){
                        System.out.println();
                        logger.error("Error en el arranque de Fashtoll por consola: " + e.getMessage());
                        System.out.println();
                    }

                    break;
            }
        }
    }

    public void readBrandsJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<BrandDTO> brandList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            this.brandService.injectBrandsFromJSON(brandList);

            logger.info("Marcas cargadas exitosamente: {}", brandList.size());
            System.out.println();
        } catch (IOException e) {
            System.out.println();
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void readClientsJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<ClientDTO> clientList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            this.clientService.injectClientsFromJSON(clientList);

            logger.info("Clientes cargados exitosamente: {}", clientList.size());
            System.out.println();
        } catch (IOException e) {
            System.out.println();
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void readProductTypesJson(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<ProductTypeRequest> productTypesList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            this.productTypeService.injectProductTypeFromJson(productTypesList);

            logger.info("Tipos de productos cargados exitosamente: {}", productTypesList.size());
            System.out.println();
        } catch (IOException e) {
            System.out.println();
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void rearProductJson(String fileName, String brandName) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.setResource(fileName);

        try (java.io.InputStream inputStream = this.resource.getInputStream()) {

            List<ProductCreateRequest> productList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
            try{
                this.productService.injectBrandProductFromJson(brandName, productList);
            } catch (ResourceNotFoundException e){
                logger.error(e.getMessage());
            }


            logger.info("Productos cargados exitosamente: {}", productList.size());
            System.out.println();
        } catch (IOException e) {
            System.out.println();
            logger.error("Error al procesar el archivo {}: {}", fileName, e.getMessage());
            System.out.println();
        }
    }

    public void adminsRegister(){
        RegisterRequest adminRequest = new RegisterRequest();
        adminRequest.setEmail("admin@example.com");
        adminRequest.setPassword("admin123");
        adminRequest.setRole(Role.categorize("ADMIN"));
        adminRequest.setName("Admin");

        this.authService.adminRegister(adminRequest);

        adminRequest.setEmail("gonso@example.com");
        adminRequest.setPassword("gonso123");
        adminRequest.setRole(Role.categorize("ADMIN"));
        adminRequest.setName("Gonso");

        this.authService.adminRegister(adminRequest);

        System.out.println();
        logger.info("Admins registrados exitosamente");
        System.out.println();
    }
}
